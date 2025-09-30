package com.ZhongHou.Ecommerce.security;

import com.ZhongHou.Ecommerce.entity.User;
import com.ZhongHou.Ecommerce.service.RedisRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    @Autowired
    private RedisRepository redisRepository;

    //private static  final long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 60L * 24L * 30L * 6L; //6months
    private static final long ACCESS_TTL = 1000L * 60L * 60L * 15L; //15hours
    private static final long REFRESH_TTL = 1000L * 60L  * 60L * 24L * 30L; // 30days

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

    public String generateRefreshToken(User user){
        return generateRefreshToken(user.getEmail());
    }
    public String generateRefreshToken(String username){
        long now = System.currentTimeMillis();
        String jti = UUID.randomUUID().toString();
        String token = Jwts.builder()
                .subject(username)
                .claim("type","refresh")
                .id(jti)
                .issuedAt(new Date(now))
                .expiration(new Date(now + REFRESH_TTL))
                .signWith(key)
                .compact();
        log.debug("Generated refresh token jti={} for {}", jti, username);
        return token;
    }

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .claim("type","access")
                .id(UUID.randomUUID().toString()) // check for redis
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ACCESS_TTL))
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

        try {
            Claims claims = extractClaims(token, c -> c);
            String type = claims.get("type", String.class);
            String jti = claims.getId();
            Date exp = claims.getExpiration();

            if (username == null || !username.equals(userDetails.getUsername())) return false;
            if (!"access".equals(type)) return false;
            if (redisRepository.findById(jti).isPresent()) return false;
            if (exp.before(new Date())) return false;
            return true;
        }catch (Exception e){
            return false;

        }
        //return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    private boolean isRevoked(String token){
        String jti = extractClaims(token, Claims::getId);
        return redisRepository.findById(jti).isPresent();
    }

    public boolean isExpired(String token){
        Date exp = extractClaims(token, Claims::getExpiration);
        return exp.before(new Date());
    }





}
