package com.ZhongHou.Ecommerce.dto.Payment;

import com.ZhongHou.Ecommerce.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class CryptoService {
    private final Mac mac = Mac.getInstance("HmacSHA512");

    @Value("${payment.vnpay.secret-key}")
    private String secretKey;

    public CryptoService() throws NoSuchAlgorithmException {
    }

    @PostConstruct
    void init() throws InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(secretKeySpec);
    }


    public String sign(String data){
        try{
            return toHexString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception e){
            throw new BusinessException("Can not sign data");
        }
    }

    //EncodingUtil
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
