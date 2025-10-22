package com.ZhongHou.Ecommerce.controller;

import com.ZhongHou.Ecommerce.dto.OrderRequest;
import com.ZhongHou.Ecommerce.dto.Payment.PaymentRequest;
import com.ZhongHou.Ecommerce.dto.Payment.VNPayService;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Order;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import com.ZhongHou.Ecommerce.service.OrderItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final VNPayService vnPayService;

    public OrderItemController(OrderItemService orderItemService, VNPayService vnPayService) {
        this.orderItemService = orderItemService;
        this.vnPayService = vnPayService;
    }

    //    @PostMapping("/create")
//    public ResponseEntity<Response> placeOrder(@RequestBody OrderRequest orderRequest) {
//        return ResponseEntity.ok(orderItemService.placeOrder(orderRequest));
//    }

    @PostMapping("/create")
    public ResponseEntity<Response> placeOrder(@RequestBody OrderRequest orderRequest,
                                               HttpServletRequest httpServletRequest) {

        Order savedOrder =  orderItemService.placeOrder(orderRequest);

        var ipAddress = httpServletRequest.getRemoteAddr();
        var orderReference =savedOrder.getOrderReference();
        var amount = savedOrder.getTotalPrice();
        var requestId = UUID.randomUUID().toString();

        log.info("IP ADDRESS {}",ipAddress);


        PaymentRequest vnpayRequest = new PaymentRequest();
        vnpayRequest.setOrderReference(orderReference); //TxRef
        vnpayRequest.setAmount(amount);
        vnpayRequest.setIpAddress(ipAddress);
        vnpayRequest.setRequestId(requestId);

        Response finalResponse = vnPayService.createVNPaymentIntent(vnpayRequest);

        return ResponseEntity.ok(finalResponse);
    }


    @PutMapping("/update-item-status/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateOrderItemStatus(@PathVariable Long orderItemId, @RequestParam String status)
    {
        return ResponseEntity.ok(orderItemService.updateOrderItemStatus(orderItemId, status));
    }



    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> filterOrderItem(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long itemId,
            @RequestParam(defaultValue = "0") int  page,
            @RequestParam(defaultValue = "100") int size
            ){
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"id"));
        OrderStatus orderStatus=status  != null ? OrderStatus.valueOf(status.toUpperCase()) :null;

        return ResponseEntity.ok(orderItemService
                                .filterOrderItems(orderStatus,startDate,endDate,itemId,pageable));
    }




}
