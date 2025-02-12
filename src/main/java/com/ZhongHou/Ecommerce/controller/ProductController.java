package com.ZhongHou.Ecommerce.controller;

import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.exception.InvalidCredentialsException;
import com.ZhongHou.Ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createProduct(
            @RequestParam Long categoryId,
            @RequestParam MultipartFile image,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price
    ){
        if (categoryId ==null || image.isEmpty() || name.isEmpty() || description.isEmpty() || price ==null){
            throw new InvalidCredentialsException("All Fields are Required");
        }
        return ResponseEntity.ok(productService.createProduct(categoryId,image,name,description,price));

    }
}
