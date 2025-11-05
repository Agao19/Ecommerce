package com.ZhongHou.Ecommerce.service;

import com.ZhongHou.Ecommerce.dto.AddressDto;
import com.ZhongHou.Ecommerce.dto.response.Response;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}
