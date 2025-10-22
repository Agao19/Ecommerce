package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.enums.PaymentGateway;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequest {

    @NotBlank(message = "Order is required")
    private String orderReference;
    private BigDecimal amount;


    private String transactionId;

    private boolean success;
    private String failureReason;


    //Vnpay
    private String requestId;
    private String ipAddress;
    private Long userId;
   // private String txnRef = orderReference





}
