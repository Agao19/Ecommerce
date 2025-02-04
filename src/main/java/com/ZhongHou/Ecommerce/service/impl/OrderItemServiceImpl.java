package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.OrderRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import com.ZhongHou.Ecommerce.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    @Override
    public Response placeOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        return null;
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        return null;
    }
}
