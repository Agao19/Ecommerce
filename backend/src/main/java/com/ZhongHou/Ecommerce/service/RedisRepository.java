package com.ZhongHou.Ecommerce.service;


import com.ZhongHou.Ecommerce.security.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<RedisToken, String> {
}
