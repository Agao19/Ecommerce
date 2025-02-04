package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class ProductServiceImpl implements ProductService {


    @Override
    public Response createProduct(Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        return null;
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, MultipartFile image, String name, String description, BigDecimal price) {
        return null;
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
