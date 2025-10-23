package com.ZhongHou.Ecommerce.repository;

import com.ZhongHou.Ecommerce.service.CategoryService;
import com.ZhongHou.Ecommerce.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class AppRunner implements CommandLineRunner {
    private final CategoryService categoryService;

    public AppRunner(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("....Caching start testing : ");
        log.info("get id6 Category -> " +  categoryService.getCategoryById(6L));
        log.info("get id7 Category -> " +  categoryService.getCategoryById(7L));
        log.info("get id8 Category -> " +  categoryService.getCategoryById(8L));
        log.info("get id9 Category -> " +  categoryService.getCategoryById(9L));
        log.info("....Caching end: ");
    }
}