package com.ZhongHou.Ecommerce.service;

import com.ZhongHou.Ecommerce.dto.AddressDto;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Address;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}
