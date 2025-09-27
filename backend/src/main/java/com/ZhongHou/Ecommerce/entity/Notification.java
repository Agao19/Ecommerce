package com.ZhongHou.Ecommerce.entity;

import com.ZhongHou.Ecommerce.enums.NotficationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String subject;

    @NotBlank(message = "repient is required")
    private String recipient;

    private String body;

    private String orderReference;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private NotficationType type;

}
