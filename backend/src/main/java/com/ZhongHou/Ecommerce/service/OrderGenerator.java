package com.ZhongHou.Ecommerce.service;

import com.ZhongHou.Ecommerce.entity.OrderReference;
import com.ZhongHou.Ecommerce.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderGenerator {

    private final OrderReferenceRepository  orderReferenceRepository;

    public String generateOrderReference(){
        String orderReference;
        do {
            orderReference = generateRandomAlphaNumberCode(10);
        }while (isBookingReferenceExist(orderReference)); //true

        saveBookingReferenceToDatabase(orderReference);;
        return orderReference;
    }

    private String generateRandomAlphaNumberCode(int length){
        String chars = "ABCDEFGHIJKLMNOQRS123456789";
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i< length; i++){
            int index = random.nextInt(chars.length());
            stringBuilder.append(chars.charAt(index)); //D54P1..
        }
        return stringBuilder.toString();
    }

    private Boolean isBookingReferenceExist(String orderReference){
        return orderReferenceRepository.findByReferenceNo(orderReference).isPresent();
    }

    //Save ReferenceNo in Table OrderReference
    private void saveBookingReferenceToDatabase(String orderReference){
        OrderReference newOrderReferenceToSave = OrderReference.builder()
                .referenceNo(orderReference)
                .build();
        orderReferenceRepository.save(newOrderReferenceToSave);
    }
}
