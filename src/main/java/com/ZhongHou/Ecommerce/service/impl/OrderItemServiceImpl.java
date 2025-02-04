package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.OrderRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import com.ZhongHou.Ecommerce.mapper.EntityDtoMapper;
import com.ZhongHou.Ecommerce.repository.OrderItemRepository;
import com.ZhongHou.Ecommerce.repository.OrderRepository;
import com.ZhongHou.Ecommerce.repository.ProductRepository;
import com.ZhongHou.Ecommerce.service.OrderItemService;
import com.ZhongHou.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {

        User user = userService.getLoginUser();

        //map order request items to order entities

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
