package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.AddressDto;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Address;
import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.repository.AddressRepository;
import com.ZhongHou.Ecommerce.service.AddressService;
import com.ZhongHou.Ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;


    @Override
    public Response saveAndUpdateAddress(AddressDto addressDto) {
        User user=userService.getLoginUser();

        Address address=user.getAddress();
        if (address==null){
            address = new Address();
            address.setUser(user);
        }
        if (addressDto.getStreet() != null) address.setStreet(addressDto.getStreet());
        if (addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if (addressDto.getState() != null) address.setState(addressDto.getState());
        if (addressDto.getZipCode() != null) address.setZipCode(addressDto.getZipCode());
        if (addressDto.getCountry() != null) address.setCountry(addressDto.getCountry());

        addressRepository.save(address);

        String message= (user.getAddress()==null) ? "Address successfully created " : "Address successfully updated";

        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }
}
