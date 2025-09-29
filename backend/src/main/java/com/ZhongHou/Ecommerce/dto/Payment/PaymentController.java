package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.entity.StripePayment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
 @RequiredArgsConstructor
 @RequestMapping("/payments")
public class PaymentController {

   // private final PaymentService paymentService;
    private final StripeService stripeService;

    @PostMapping("/pay")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(stripeService.createPaymentIntent(paymentRequest));
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
