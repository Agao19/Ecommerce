package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.CategoryDto;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Override
    public Response createCategory(CategoryDto categoryRequest) {
        return null;
    }

    @Override
    public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
        return null;
    }

    @Override
    public Response getAllCategories() {
        return null;
    }

    @Override
    public Response getCategoryById(Long categoryId) {
        return null;
    }

    @Override
    public Response deleteCategory(Long categoryRequest) {
        return null;
    }
}
