package com.ZhongHou.Ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

//@Data
//@Entity
//@Table(name = "momo_payments")
public class MomoPayment {
    @Id
    private Long id; // trùng với payment_id

    @OneToOne
    @MapsId
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String partnerCode;
    private String requestId;
    private String orderId;
    private String payUrl;
    private String deeplink;
    private String signature;
}
