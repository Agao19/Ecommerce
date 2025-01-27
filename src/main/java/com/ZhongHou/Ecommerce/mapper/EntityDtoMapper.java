package com.ZhongHou.Ecommerce.mapper;

import com.ZhongHou.Ecommerce.dto.*;
import com.ZhongHou.Ecommerce.entity.*;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    //user entity to user DTO

    public UserDto mapUserToDtoBasic(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setName(user.getName());
        return userDto;
    }

    //Address to DTO basic
    public AddressDto mapAddressToDtoBasic(Address add){
        AddressDto addressDto = new AddressDto();
        addressDto.setId(add.getId());
        addressDto.setCity(add.getCity());
        addressDto.setState(add.getState());
        addressDto.setStreet(add.getStreet());
        addressDto.setCountry(add.getCountry());
        addressDto.setZipCode(add.getZipCode());
        return addressDto;
    }

    //Category to DTO
    public CategoryDto mapCategoryToDtoBasic(Category category){
        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setId(categoryDto.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    //OrderItem to Dto
    public OrderItemDto mapOrderItemToDtoBasic(OrderItem orderItem){
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setStatus(orderItemDto.getStatus());
        orderItemDto.setCreatedAt(orderItem.getCreatedAt());

        return  orderItemDto;
    }

    //Product to DTO
    public ProductDto mapProductToDtoBasic(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        return productDto;
    }

    /////
    public UserDto mapUserToDtoPlusAddress(User user){
        UserDto userDto = mapUserToDtoBasic(user);
        if(user.getAddress() != null) {
            AddressDto addressDto = mapAddressToDtoBasic(user.getAddress());
            userDto.setAddress(addressDto);
        }
        return userDto;
    }

    //orderItem to Dto plus product
    public OrderItemDto mapOrderItemToDtoPlusProduct(OrderItem orderItem){
        OrderItemDto orderItemDto = mapOrderItemToDtoBasic(orderItem);

        if (orderItemDto.getProduct() != null){
            ProductDto productDto= mapProductToDtoBasic(orderItem.getProduct());
            orderItemDto.setProduct(productDto);
        }
        return orderItemDto;
    }

    //OrderItem to Dto plus product adn user






}
