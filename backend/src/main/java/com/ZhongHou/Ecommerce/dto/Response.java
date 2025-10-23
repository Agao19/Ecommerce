package com.ZhongHou.Ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int status;
    private String message;
    //@JsonIgnore //testing cache
    private final LocalDateTime timestamp=LocalDateTime.now();

    private String token;
    private String role;
    private Integer expirationTime;
    private String refreshToken;

    private int totalPage;
    private long totalElement;

    private AddressDto addressDto;

    private UserDto user;
    private List<UserDto> userList;

    private CategoryDto category;
    private List<CategoryDto> categoryList;

    private ProductDto product;
    private List<ProductDto> productList;

    private OrderItemDto orderItem;
    private List<OrderItemDto> orderItemList;

    private OrderDto order;
    private List<UserDto> orderList;



    //InitPaymentResponse
    private String vnpUrl;

}
