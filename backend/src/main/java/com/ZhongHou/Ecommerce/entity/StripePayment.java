package com.ZhongHou.Ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

//@Data
//@Entity
//@Table(name = "stripe_payments")
public class StripePayment {
    @Id
    private Long id; // trùng với payment_id

    @OneToOne
    @MapsId
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String paymentIntentId;
    private String clientSecret;
    private String checkoutSessionId;
}