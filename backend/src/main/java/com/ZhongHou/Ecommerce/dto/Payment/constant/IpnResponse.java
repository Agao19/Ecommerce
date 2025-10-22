package com.ZhongHou.Ecommerce.dto.Payment.constant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class IpnResponse {
    
    @JsonProperty("RspCode")
    private String responseCode;

    @JsonProperty("Message")
    private String message;

    public IpnResponse(String responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }
}
