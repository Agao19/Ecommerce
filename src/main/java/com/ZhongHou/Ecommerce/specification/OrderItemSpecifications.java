package com.ZhongHou.Ecommerce.specification;

import com.ZhongHou.Ecommerce.entity.OrderItem;
import com.ZhongHou.Ecommerce.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderItemSpecifications {

    public static Specification<OrderItem> hasStatus(OrderStatus status){
        return (((root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"),status):null));

    }

}
