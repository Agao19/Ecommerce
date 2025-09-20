package com.ZhongHou.Ecommerce.specification;

import com.ZhongHou.Ecommerce.entity.OrderItem;
import com.ZhongHou.Ecommerce.enums.OrderStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderItemSpecifications {

    /** Filter oderItem by Status*/
    public static Specification<OrderItem> hasStatus(OrderStatus status){
        return new Specification<OrderItem>() {

            @Override
            public Predicate toPredicate(Root<OrderItem> root, 
            CriteriaQuery<?> query, 
            CriteriaBuilder criteriaBuilder) {
                // TODO Auto-generated method stub
               if (status != null) {
                return criteriaBuilder.equal(root.get("status"), status);
               } else {
                return null ;
               }
            }
        };
    
        
    }

    /** Filter orderItems by data range*/
    public static Specification<OrderItem> createdBetween(LocalDateTime startDate, LocalDateTime endDate){

        return new Specification<OrderItem>() {

            @Override
            public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                                            if (startDate != null && endDate != null){
                                                return criteriaBuilder.between(root.get("createdAt"),startDate,endDate);
                                            } else if (startDate != null){
                                                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),startDate);
                                            } else if (endDate != null) {
                                                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),endDate);
                                            }else {
                                                return null;
                                            }
            }
        };
    }

    /** Filter orderitems by item id*/
    public static Specification<OrderItem> hasItemId(Long itemId){
        return ((root, query, criteriaBuilder) ->
                itemId != null ? criteriaBuilder.equal(root.get("id"),itemId):null);
    }


}
