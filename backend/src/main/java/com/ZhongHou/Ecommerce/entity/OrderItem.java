package com.ZhongHou.Ecommerce.entity;

import com.ZhongHou.Ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private BigDecimal price;
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY) //1 user có nhiều orderitem, nhưng mỗi orderItem sẽ có một user liên kết
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // một sản phẩm có thể xuat hien trong nhiều đơn hàng (order) khác nhau
                                        // mỗi đơn hàng sẽ có nhiều danh mục (orderitems) chứa san phẩm đó.
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "created_at")
    private final LocalDateTime createdAt=LocalDateTime.now();



}
