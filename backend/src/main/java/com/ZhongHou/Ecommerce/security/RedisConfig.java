package com.ZhongHou.Ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

//@Configuration
public class RedisConfig {
   // @Bean
    public LettuceConnectionFactory RedisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }


}
