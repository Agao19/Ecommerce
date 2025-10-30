package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.CategoryDto;
import com.ZhongHou.Ecommerce.dto.response.Response;
import com.ZhongHou.Ecommerce.entity.Category;
import com.ZhongHou.Ecommerce.exception.AppException;
import com.ZhongHou.Ecommerce.exception.ErrorCode;
import com.ZhongHou.Ecommerce.mapper.EntityDtoMapper;
import com.ZhongHou.Ecommerce.repository.CategoryRepository;
import com.ZhongHou.Ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EntityDtoMapper entityDtoMapper;

    //Testing caching
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
   // @CacheEvict(value = "categories", allEntries = true)
    public Response createCategory(CategoryDto categoryRequest) {
        Category category=new Category();
        category.setName(categoryRequest.getName());
        categoryRepository.save(category);

        return Response.builder()
                .status(200)
                .message("Category created successfully")
                .build();
    }

    @Override
    //@CacheEvict(value = "categories", allEntries = true)
    public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
        Category category=categoryRepository.findById(categoryId)
        .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        category.setName(categoryRequest.getName());
        categoryRepository.save(category);
        return Response.builder()
                .status(200)
                .message("Category updated successfully")
                .build();
    }

    // @Override
    // public Response getAllCategories( ) {
    //     List<Category> categories=categoryRepository.findAll();
       
    //     List<CategoryDto> categoryDtoList=categories.stream()
    //             //.map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
    //             .map(item -> entityDtoMapper.mapCategoryToDtoBasic(item))
    //             .collect(Collectors.toList());

    //     return Response.builder()
    //             .status(200)
    //             .categoryList(categoryDtoList)
    //             .build();
    // }

    @Override
   // @Cacheable("categories")
    public Response getAllCategories( ) {
        List<Category> categories=categoryRepository.findAll();
       
        List<CategoryDto> categoryDtoList= new ArrayList<>();
        
        for (Category category : categories) {
            CategoryDto categoryDto = entityDtoMapper.mapCategoryToDtoBasic(category);
            categoryDtoList.add(categoryDto);
        }

        return Response.builder()
                .status(200)
                .categoryList(categoryDtoList)
                .build();
    }

    @Override
    @Transactional
    //@Cacheable(value="categories", key = "#categoryId")
    public Response getCategoryById(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
        .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        CategoryDto categoryDto=entityDtoMapper.mapCategoryToDtoBasic(category);

       // simulateSlowService(); //testing cached

        return Response.builder()
                .status(200)
                .category(categoryDto)
                .build();
    }

    @Override
    public Response deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        categoryRepository.delete(category);
        return Response.builder()
                .status(200)
                .message("Category was deleted successfully")
                .build();
    }
}
