package com.ZhongHou.Ecommerce.service;

import com.ZhongHou.Ecommerce.dto.OrderRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Order;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;

public interface OrderItemService {

    Order placeOrder(OrderRequest orderRequest);

    Response updateOrderItemStatus(Long orderItemId, String status);

    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);



}
