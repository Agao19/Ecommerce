package com.ZhongHou.Ecommerce.dto.Payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
 @RequiredArgsConstructor
 @RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

     @PostMapping("/pay")
     public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest){
         return ResponseEntity.ok(paymentService.createPaymentIntent(paymentRequest));
     }


}
