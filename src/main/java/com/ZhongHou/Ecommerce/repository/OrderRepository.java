package com.ZhongHou.Ecommerce.repository;

import com.ZhongHou.Ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
