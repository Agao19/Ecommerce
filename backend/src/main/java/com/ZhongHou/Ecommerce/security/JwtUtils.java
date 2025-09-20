package com.ZhongHou.Ecommerce.security;

import com.ZhongHou.Ecommerce.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    private static  final long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 24L * 30L * 6L; //mong doi 6months

    private SecretKey key;

    @Value("${secreteJwtString}")
    private String secreteJwtString; //gia tri o app propeties phai is 32 ki tu hoac lon hon

    @PostConstruct
    private void init(){
        byte[] keyBytes=secreteJwtString.getBytes(StandardCharsets.UTF_8);
        this.key=new SecretKeySpec(keyBytes,"HmacSHA256");
    }
    public String generateToken(User user){
        return generateToken(user.getEmail());
    }
    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME_IN_MILLISEC))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key)
                .build()
                .parseSignedClaims(token) //ko dc dung encrypted (jwe)
                .getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return  extractClaims(token,Claims::getExpiration).before(new Date());
    }


}
