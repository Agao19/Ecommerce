package com.ZhongHou.Ecommerce.Payment;

import com.ZhongHou.Ecommerce.Payment.constant.IpnResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IpnHandler {
    IpnResponse process(Map<String, String> params) throws UnsupportedEncodingException;
}
