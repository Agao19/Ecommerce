package com.ZhongHou.Ecommerce.repository;

import com.ZhongHou.Ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository  extends JpaRepository<Payment,Long> {
}
