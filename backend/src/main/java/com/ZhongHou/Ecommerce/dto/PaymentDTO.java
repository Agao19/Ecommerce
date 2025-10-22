package com.ZhongHou.Ecommerce.dto;

import com.ZhongHou.Ecommerce.enums.PaymentGateway;
import com.ZhongHou.Ecommerce.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO {


    private Long id;

    private String transactionId;

    private BigDecimal amount;

    private PaymentGateway paymentMethod;

    private LocalDateTime paymentDate;

    private PaymentStatus status;

    private String orderReference;
    private String failureReason;

    private String approvalLink;

}
