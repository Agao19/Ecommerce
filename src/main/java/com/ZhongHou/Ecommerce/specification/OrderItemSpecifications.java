package com.ZhongHou.Ecommerce.specification;

import com.ZhongHou.Ecommerce.entity.OrderItem;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderItemSpecifications {

    public static Specification<OrderItem> hasStatus(OrderStatus status){
        return (((root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"),status):null));

    }

    public static Specification<OrderItem> createdBetween(LocalDateTime startDate, LocalDateTime endDate){
        return (((root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null){
                return criteriaBuilder.between(root.get("createdAt"),startDate,endDate);
            } else if (startDate != null){
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),startDate);
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),endDate);
            }else {
                return null;
            }
        }));
    }



}
