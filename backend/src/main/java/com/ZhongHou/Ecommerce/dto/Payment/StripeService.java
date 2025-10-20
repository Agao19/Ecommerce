package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.entity.Order;
import com.ZhongHou.Ecommerce.entity.Payment;
import com.ZhongHou.Ecommerce.enums.PaymentGateway;
import com.ZhongHou.Ecommerce.enums.PaymentStatus;
import com.ZhongHou.Ecommerce.exception.NotFoundException;
import com.ZhongHou.Ecommerce.repository.OrderRepository;
import com.ZhongHou.Ecommerce.repository.PaymentRepository;
import com.ZhongHou.Ecommerce.service.impl.NotiService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final NotiService emailService;

    @Value("${stripe.api.secret.key}")
    private String secreteKey;


    public String createPaymentIntent(PaymentRequest paymentRequest){
        log.info("INSIDE stripe createPaymentIntent");
        Stripe.apiKey = secreteKey;

        String orderReference = paymentRequest.getOrderReference();

        Order order =orderRepository.findByOrderReference(orderReference)
                .orElseThrow(()-> new NotFoundException("Item not found"));

        if (order.getPaymentStatus() == PaymentStatus.COMPLETED){
            throw new NotFoundException("Completed Payment");
        }

        if (order.getTotalPrice().compareTo(paymentRequest.getAmount()) != 0) {
            throw new NotFoundException("Payment Amount doesnt map. Please contact our customer support agent ");
        }

        try{
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                    .setCurrency("usd")
                    .putMetadata("orderReference",orderReference)
                    .build();

            PaymentIntent intent =PaymentIntent.create(params);

            //save to db
            Payment payment = new Payment();
            payment.setPaymentGateWay(PaymentGateway.STRIPE);
            payment.setAmount(paymentRequest.getAmount());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setPaymentDate(LocalDateTime.now());
                payment.setOrderReference(orderReference);
            payment.setTransactionId(intent.getId());
            payment.setUser(order.getOrderItemList()
                    .stream()
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("User dont have items")).getUser());

            paymentRepository.save(payment);

            //save  => Processing khi còn chưa thanh toán
            if(order.getPaymentStatus() == PaymentStatus.PENDING){
                order.setPaymentStatus(PaymentStatus.PROCESSING);
            }
            orderRepository.save(order);

            return intent.getClientSecret(); // clientSecret = Payment ID + secret token


        }catch (Exception e){
//            throw new RuntimeException("Error creating payment unique transaction id");
            log.error("Stripe error", e);
            throw new RuntimeException("Payment failed: " + e.getMessage(), e);
        }
    }

}
