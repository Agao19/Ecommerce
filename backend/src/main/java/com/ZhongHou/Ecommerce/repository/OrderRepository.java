package com.ZhongHou.Ecommerce.repository;

import com.ZhongHou.Ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByOrderReference(String o);
}
