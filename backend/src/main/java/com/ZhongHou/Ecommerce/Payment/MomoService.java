package com.ZhongHou.Ecommerce.Payment;

import com.ZhongHou.Ecommerce.entity.Order;
import com.ZhongHou.Ecommerce.entity.Payment;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.enums.PaymentGateway;
import com.ZhongHou.Ecommerce.enums.PaymentStatus;
import com.ZhongHou.Ecommerce.exception.AppException;
import com.ZhongHou.Ecommerce.exception.ErrorCode;
import com.ZhongHou.Ecommerce.repository.OrderRepository;
import com.ZhongHou.Ecommerce.repository.PaymentRepository;
import com.ZhongHou.Ecommerce.repository.UserRepository;
import com.ZhongHou.Ecommerce.service.impl.NotiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
@Slf4j
public class MomoService {
   // private static final String AMOUNT = "2000";

    private  OrderRepository orderRepository;
    private  PaymentRepository paymentRepository;
    private  NotiService emailService;
    private  UserRepository userRepository;
    private  ObjectMapper mapper = new ObjectMapper();

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.returnUrl}")
    private String returnUrl;

    @Value("${momo.notifyUrl}")
    private String notifyUrl;

    public Map<String,String> createMomoPaymentIntent(String orderReference, Long userId) throws Exception {
        log.info("INSIDE MOMO createPaymentIntent");

        Order order =orderRepository.findByOrderReference(orderReference)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new AppException(ErrorCode.SERVICE_COMPLETED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));


        // Số tiền thực từ order
        BigDecimal amount = order.getTotalPrice();
        String amountStr = amount.stripTrailingZeros().toPlainString();

        String orderId=UUID.randomUUID().toString();  // MoMo orderId (merchant side)
        String requestId = UUID.randomUUID().toString();
        String orderInfo = "Upgrade Premium for user: " + user.getName();
        String extraData = userId.toString(); //compare for callback, rawData confirm extraData

        String rawData = buildRawData(orderId,requestId,orderInfo,extraData,amountStr);
        String signature = signSHA256(rawData, secretKey);

        log.debug("==== MOMO SIGN ====");
        log.debug("RawData: {}", rawData);
        log.debug("Signature: {}", signature);


        //Build payloadJson
        ObjectNode payload = mapper.createObjectNode();
        payload.put("partnerCode", partnerCode);
        payload.put("accessKey", accessKey);
        payload.put("requestId", requestId);
        payload.put("amount", amountStr);
        payload.put("orderId", orderId);
        payload.put("orderInfo", orderInfo);
        payload.put("redirectUrl", returnUrl);
        payload.put("ipnUrl", notifyUrl);
        payload.put("extraData", extraData); // ✅ khớp với rawData
        payload.put("requestType", "captureWallet");
        payload.put("signature", signature);

        // Gửi request
        JsonNode jsonResponse = doPost(endpoint, payload);

        int resultCode = jsonResponse.has("resultCode") ? jsonResponse.get("resultCode").asInt() : -999;
        String message = jsonResponse.has("message") ? jsonResponse.get("message").asText() : "";


        if (resultCode != 0) {
            log.error("MoMo init failed resultCode={} message={} response={}", resultCode, message, jsonResponse.toPrettyString());
            throw new RuntimeException("MoMo init failed: " + message);
        }

        String payUrl = jsonResponse.get("payUrl").asText();
        String transId = jsonResponse.has("transId") ? jsonResponse.get("transId").asText() : null;


        // Lưu payment (transId lúc này có thể null cho đến khi thanh toán thành công)
        Payment payment = new Payment();
        payment.setPaymentGateWay(PaymentGateway.MOMO);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setOrderReference(orderReference);
        payment.setTransactionId(transId); // có thể cập nhật lại khi IPN trả về
        payment.setUser(user);
        paymentRepository.save(payment);

        // Cập nhật trạng thái order -> PROCESSING (đang chờ thanh toán)
        if (order.getPaymentStatus() == PaymentStatus.PENDING) {
            order.setPaymentStatus(PaymentStatus.PROCESSING);
            orderRepository.save(order);
        }

        return Map.of(
                "payUrl", payUrl,
                "orderId", orderId,
                "requestId", requestId,
                "transId", transId != null ? transId : ""
        );

    }

    private JsonNode doPost(String url, ObjectNode body) throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(15_000);
            conn.setRequestProperty("Content-Type", "application/json");

            String json = mapper.writeValueAsString(body);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            InputStream is = status >= 200 && status < 300
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            String response = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            log.debug("MoMo raw response: {}", response);

            return mapper.readTree(response);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String buildRawData(String orderId,
                                String requestId,
                                String orderInfo,
                                String extraData,
                                String amount) {

        return "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + notifyUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + returnUrl +
                "&requestId=" + requestId +
                "&requestType=captureWallet";
    }

    private String signSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash); // trả về lowercase hex
    }

}
