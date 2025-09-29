//package com.ZhongHou.Ecommerce.dto.Payment;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PaymentService {
//
//    private  MomoService momoService;
//    private  StripeService stripeService;
//
//    public Object initiate(PaymentRequest request) throws Exception {
//        log.info("Initiate payment gateway={}, orderRef={}", request.getPaymentGateway(), request.getOrderReference());
//        if (request.getPaymentGateway() == null) {
//            throw new IllegalArgumentException("Payment gateway is required");
//        }
//
//        switch (request.getPaymentGateway()) {
//            case STRIPE:
//                return Map.of(
//                        "gateway", "STRIPE",
//                        "clientSecret", stripeService.createPaymentIntent(request),
//                        "orderReference", request.getOrderReference(),
//                        "status", "PENDING"
//                );
//            case MOMO:
//                return momoService.createMomoPaymentIntent(request.getOrderReference(), request.getUserId());
//            default:
//                throw new IllegalArgumentException("Unsupported gateway: " + request.getPaymentGateway());
//        }
//    }
//}
