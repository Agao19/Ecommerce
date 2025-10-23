package com.ZhongHou.Ecommerce.security;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

//@Configuration
public class RedisConfig {
    // @Bean
    public LettuceConnectionFactory RedisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    //@Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration("categories", // Tên cache dùng trong @Cacheable)
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10)) // TTL cụ thể
                                .serializeValuesWith(RedisSerializationContext
                                        .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                )

                .withCacheConfiguration("products", //
                        RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(RedisSerializationContext
                                        .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                );


        // (TẤT CẢ cache đều dùng JSON mà không cần định nghĩa tên)
        // return (builder) -> builder
        //         .cacheDefaults(
        //                 RedisCacheConfiguration.defaultCacheConfig()
        //                         .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
        //         );


    }
}