package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.NotificationDTO;
import com.ZhongHou.Ecommerce.dto.OrderItemDto;
import com.ZhongHou.Ecommerce.dto.OrderRequest;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Order;
import com.ZhongHou.Ecommerce.entity.OrderItem;
import com.ZhongHou.Ecommerce.entity.Product;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import com.ZhongHou.Ecommerce.exception.NotFoundException;
import com.ZhongHou.Ecommerce.mapper.EntityDtoMapper;
import com.ZhongHou.Ecommerce.repository.NotificationRepository;
import com.ZhongHou.Ecommerce.repository.OrderItemRepository;
import com.ZhongHou.Ecommerce.repository.OrderRepository;
import com.ZhongHou.Ecommerce.repository.ProductRepository;
import com.ZhongHou.Ecommerce.service.OrderGenerator;
import com.ZhongHou.Ecommerce.service.OrderItemService;
import com.ZhongHou.Ecommerce.service.UserService;
import com.ZhongHou.Ecommerce.specification.OrderItemSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;

    //
    private final OrderGenerator orderGenerator;
    private final NotiService notiService;
    private final NotificationRepository notificationRepository;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {

        User user = userService.getLoginUser();

        //map order request items to order entities
        List<OrderItem> orderItems=orderRequest.getItems()
                .stream()
                .map(orderItemRequest -> {
                    Product product=productRepository.findById(orderItemRequest.getProductId())
                            .orElseThrow(() -> new NotFoundException("Product not found"));

                    OrderItem orderItem=new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemRequest.getQuantity());
                    orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))); //set price according
                    orderItem.setStatus(OrderStatus.PENDING);
                    orderItem.setUser(user);
                    return orderItem;
                }).collect(Collectors.toList());

        //Tinh tong tien
        BigDecimal totalPrice= orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) >0
                ? orderRequest.getTotalPrice()
                :orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);

        //create order entity
        String orderReference = orderGenerator.generateOrderReference();

        Order order=new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);

        order.setOrderReference(orderReference); //Dua vao de gui mail


        //set the order reference in each orderitem
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderRepository.save(order);


        //SEND mail xac nhan
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("ORDER SUCCESSFULLY")
                .body(String.format("Order successfully, \n Please process with payment , Thansk!!"))
                .orderReference(orderReference)
                .build();

        notiService.sendEmail(notificationDTO);

        return Response.builder()
                .status(200)
                .message("Order was successfully placed")
                .build();
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem=orderItemRepository.findById(orderItemId)
                .orElseThrow(()->new NotFoundException("Order item not found"));

        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepository.save(orderItem);

        return Response.builder()
                .status(200)
                .message("Order status updated successfully")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, 
                                LocalDateTime startDate, 
                                LocalDateTime endDate, 
                                Long itemId, 
                                Pageable pageable) {
        
        Specification<OrderItem> spec= Specification
                .where(OrderItemSpecifications.hasStatus(status))
                .and(OrderItemSpecifications.createdBetween(startDate, endDate))
                .and(OrderItemSpecifications.hasItemId(itemId));

        Page<OrderItem> orderItemPage=orderItemRepository.findAll(spec,pageable);
                                                                
        if (orderItemPage.isEmpty()){
            throw new NotFoundException("No order found");
        }
        List<OrderItemDto> orderItemDtos=orderItemPage.getContent().stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElement(orderItemPage.getTotalElements())
                .build();
    }
}
