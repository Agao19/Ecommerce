package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.dto.Payment.constant.Locale;
import com.ZhongHou.Ecommerce.dto.Payment.constant.VNPayParams;
import com.ZhongHou.Ecommerce.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class VNPayService  implements PaymentService{

    public static final String VERSION = "2.1.0"; //version api vnpay
    public static final String COMMAND = "pay"; //api thanh toan
    public static final String ORDER_TYPE = "190000"; // ma danh muc hang hoa cua VNPAY
    public static final long DEFAULT_MULTIPLIER = 100L; //vnpay phai * 100L

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode; //ma web

    @Value("${payment.vnpay.init-payment-url}")
    private String initPaymentPrefixUrl;

    @Value("${payment.vnpay.return-url}")
    private String returnUrlFormat;

    @Value("${payment.vnpay.timeout}")
    private Integer paymentTimeout;

    private final CryptoService cryptoService;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public Response createVNPaymentIntent(PaymentRequest paymentRequest) {

        var amount =  paymentRequest.getAmount().multiply(BigDecimal.valueOf(DEFAULT_MULTIPLIER)).longValue();  //1.amount * 100
        var txnRef = paymentRequest.getOrderReference();  //2. OderReference
        var returnUrl = buildReturnUrl(txnRef);   //3. FE redirect by URL

        var vnCalendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        var createdDate = formatter.format(vnCalendar.getTime());
        vnCalendar.add(Calendar.MINUTE, paymentTimeout);
        var expiredDate = formatter.format(vnCalendar.getTime());

        var ipAddress = paymentRequest.getIpAddress();      //ipClient
        var orderInfo = buildPaymentDetail(paymentRequest); //thong bao
        var requestId= paymentRequest.getRequestId(); //optional

        //Params
        Map<String, String> params = new HashMap<>();

        params.put(VNPayParams.VERSION, VERSION);
        params.put(VNPayParams.COMMAND, COMMAND);

        params.put(VNPayParams.TMN_CODE, tmnCode);
        params.put(VNPayParams.AMOUNT, String.valueOf(amount));

        params.put(VNPayParams.CREATED_DATE, createdDate);
        params.put(VNPayParams.CURRENCY, "VND");

        params.put(VNPayParams.TXN_REF, txnRef);;
        params.put(VNPayParams.RETURN_URL, returnUrl);


        params.put(VNPayParams.EXPIRE_DATE, expiredDate);

        params.put(VNPayParams.IP_ADDRESS, ipAddress);
        params.put(VNPayParams.LOCALE, Locale.VIETNAM.getCode());

        params.put(VNPayParams.ORDER_TYPE, ORDER_TYPE);
        params.put(VNPayParams.ORDER_INFO, orderInfo);


        var initPaymentUrl= buildInitPaymentUrl(params);

        log.info("initPaymentUrl:{}",initPaymentUrl);

        return Response.builder()
                .vnpUrl(initPaymentUrl)
                .build();

    }


    private String buildPaymentDetail(PaymentRequest paymentRequest) {
        return String.format("Thanh toan don dat hang %s", paymentRequest.getOrderReference());
    }


    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }

    //Kiểm tra chữ ký khi nhận về IPN
    public boolean verifyIpn(Map<String, String> params) throws UnsupportedEncodingException {
        var reqSecureHash = params.get(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH_TYPE);

        var hashPayload = new StringBuilder();
        var fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {

                //Build hash data
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(URLEncoder.encode(fieldValue,  StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    hashPayload.append("&");
                }
            }
        }

        var secureHash = cryptoService.sign(hashPayload.toString());

        return secureHash.equals(reqSecureHash);
    }


    private String buildInitPaymentUrl(Map<String, String> params) {

        var fieldNames = new ArrayList<String>(params.keySet());
        Collections.sort(fieldNames); //1. Sort field names

        var hashPayload = new StringBuilder();
        var query = new StringBuilder(); //non theard-safe

        var itr = fieldNames.iterator();

        while (itr.hasNext()) {
            var fieldName =  itr.next();
            var fieldValue = params.get(fieldName);

            if ((fieldValue !=  null) && (!fieldValue.isEmpty())) {
                String encodedFieldValue;
                String encodedFieldName;

                try {
                    encodedFieldValue =  URLEncoder.encode(fieldValue,  StandardCharsets.US_ASCII.toString());
                    encodedFieldName =  URLEncoder.encode(fieldName,  StandardCharsets.US_ASCII.toString());
                }
                catch (UnsupportedEncodingException e) {
                    log.error("Failed to URL encode params", e);
                    throw new RuntimeException("Failed to URL encode params", e);
                }


                //Build hash data
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(encodedFieldValue);

                //Build query (key encoded = value encoded)

                    query.append(encodedFieldName);;
                    query.append("=");
                    query.append(encodedFieldValue);


                if (itr.hasNext()) {
                    query.append("&");
                    hashPayload.append("&");
                }
            }
        }

        //Build secureHash
        var secureHash = cryptoService.sign(hashPayload.toString());

        //finalize Query
        query.append("&vnp_SecureHash=");
        query.append(secureHash);

        return initPaymentPrefixUrl + "?" + query;

    }


}