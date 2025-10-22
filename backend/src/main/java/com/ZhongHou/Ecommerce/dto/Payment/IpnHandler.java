package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.dto.Payment.constant.IpnResponse;
import com.ZhongHou.Ecommerce.dto.Response;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IpnHandler {
    IpnResponse process(Map<String, String> params) throws UnsupportedEncodingException;
}
