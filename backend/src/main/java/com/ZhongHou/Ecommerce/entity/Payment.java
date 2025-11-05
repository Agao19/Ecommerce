package com.ZhongHou.Ecommerce.entity;

import com.ZhongHou.Ecommerce.enums.PaymentGateway;
import com.ZhongHou.Ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateWay;

    //ID chung
    private String transactionId;

    private String orderReference;
    private String failureReason;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
