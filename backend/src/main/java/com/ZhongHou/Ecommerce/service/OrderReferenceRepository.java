package com.ZhongHou.Ecommerce.service;

import com.ZhongHou.Ecommerce.entity.OrderReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderReferenceRepository extends JpaRepository <OrderReference, Long> {

    Optional<OrderReference> findByReferenceNo(String referenceNo);

}
