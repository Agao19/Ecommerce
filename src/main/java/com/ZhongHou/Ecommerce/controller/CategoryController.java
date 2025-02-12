package com.ZhongHou.Ecommerce.controller;

import com.ZhongHou.Ecommerce.dto.CategoryDto;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }



    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }



}
