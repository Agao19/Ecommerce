package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Category;
import com.ZhongHou.Ecommerce.entity.Product;
import com.ZhongHou.Ecommerce.exception.NotFoundException;
import com.ZhongHou.Ecommerce.mapper.EntityDtoMapper;
import com.ZhongHou.Ecommerce.repository.CategoryRepository;
import com.ZhongHou.Ecommerce.repository.ProductRepository;
import com.ZhongHou.Ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()->new NotFoundException("Category not found"));
        //String productImageUrl=

        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        //product.setImageUrl(productImageUrl);

        productRepository.save(product);

        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        Product product=productRepository.findById(productId).orElseThrow(()->new NotFoundException("Product Not found"));

    }

    @Override
    public Response deleteProduct(Long productId) {
        return null;
    }

    @Override
    public Response getProductById(Long productId) {
        return null;
    }

    @Override
    public Response getAllProduct() {
        return null;
    }

    @Override
    public Response getProductsByCategory(Long categoryId) {
        return null;
    }

    @Override
    public Response searchProduct(String searchValue) {
        return null;
    }
}
