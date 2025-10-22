package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.dto.Payment.constant.IpnResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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


    @GetMapping("/vnpay_ipn") // nhận
    IpnResponse processIpn(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {
        log.info("[VNPay Ipn] Params: {}", params);
        return ipnHandler.process(params);
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
