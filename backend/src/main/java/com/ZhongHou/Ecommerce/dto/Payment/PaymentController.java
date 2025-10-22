package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.dto.Payment.constant.IpnResponse;
import com.ZhongHou.Ecommerce.dto.Payment.constant.VnpIpnResponseConst;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

@RestController
 @RequiredArgsConstructor
 @RequestMapping("/payments")
@Slf4j
public class PaymentController {

   // private final PaymentService paymentService;
    private final StripeService stripeService;
    private final IpnHandler ipnHandler;
    @PostMapping("/pay")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(stripeService.createPaymentIntent(paymentRequest));
    }


    @PostMapping("/vnpay_ipn") // nhận từ VNPAY 
    IpnResponse processIpn(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {
        return ipnHandler.process(params);
    }

    //test return url
    @GetMapping("/vnpay_return")
    public void processIpntestReturnUrl(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        IpnResponse res = ipnHandler.process(params);
        if (res.getResponseCode().equals("00")) {
            // Redirect về Frontend với thông tin thành công
            String frontendUrl = "https://sandbox.vnpayment.vn/apis/vnpay-demo";
            response.sendRedirect(frontendUrl);
        } else {
            String frontendUrl = "abc";
            response.sendRedirect(frontendUrl);
        }
        log.info("[VNPay Ipn Test] Params: {}", params);
    }



//     @PostMapping("/stripe")
//     public ResponseEntity<?> createPaymentStripeIntent(@RequestBody PaymentRequest paymentRequest) throws Exception {
//         return ResponseEntity.ok(paymentService.initiate(paymentRequest));
//     }
//
//    @PostMapping("/momo")
//    public ResponseEntity<?> createPaymentMomoIntent(@RequestBody PaymentRequest paymentRequest) throws Exception {
//        return ResponseEntity.ok(paymentService.initiate(paymentRequest));
//    }



//    @PostMapping("/initiate")
//    public ResponseEntity<?> initiate(@Valid @RequestBody PaymentRequest paymentRequest) throws Exception {
//        if (paymentRequest.getPaymentGateway() == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "paymentGateway is required"));
//        }
//        return ResponseEntity.ok(paymentService.initiate(paymentRequest));
//    }

}
