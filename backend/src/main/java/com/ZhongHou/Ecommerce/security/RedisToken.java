package com.ZhongHou.Ecommerce.security;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("jwt_tokens")
public class RedisToken {

    @Id
    private String jwtId; //"refresh:{jti}" or "blacklist:{jti}

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiredRedisTime;


}
