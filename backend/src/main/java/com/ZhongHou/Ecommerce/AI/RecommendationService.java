package com.ZhongHou.Ecommerce.AI;

import com.ZhongHou.Ecommerce.dto.ProductDto;
import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Product;
import com.ZhongHou.Ecommerce.mapper.EntityDtoMapper;
import com.ZhongHou.Ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final EntityDtoMapper entityDtoMapper;


    public Response getSimilarProducts(Long productId) {

        // Tạo key giống script Python
        String key = "product:" + productId + ":similar";

        try {

            // (1) Lấy chuỗi JSON từ Redis
            String jsonProductIds = stringRedisTemplate.opsForValue().get(key);

            if (jsonProductIds == null || jsonProductIds.isEmpty()) {
                return null;
            }

            // (2) Parse chuỗi JSON (ví dụ: "[5, 2, 7]") thành List<Long>
            List<Long> productIds = objectMapper.readValue(jsonProductIds, new TypeReference<List<Long>>() {});

            if (productIds.isEmpty()) {
                return null;
            }

            List<Product> products = new ArrayList<>();

            // (3) Lấy thông tin sản phẩm đầy đủ từ MySQL
            // Lấy 1 lúc nhiều sản phẩm
           products =  productRepository.findAllById(productIds);

            List<ProductDto> productDtoList=products.stream()
                    .map(entityDtoMapper::mapProductToDtoBasic)
                    .collect(Collectors.toList());

            return Response.builder()
                    .status(200)
                    .message("Recommendations retrieved successfully.")
                    .productList(productDtoList)
                    .build();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.builder()
                    .status(500) // 500 Lỗi máy chủ nội bộ
                    .message("Error processing JSON: " + e.getMessage())
                    .build();
        }



    }
}
