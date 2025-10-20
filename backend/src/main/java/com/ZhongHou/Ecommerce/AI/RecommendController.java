package com.ZhongHou.Ecommerce.AI;

import com.ZhongHou.Ecommerce.dto.Response;
import com.ZhongHou.Ecommerce.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendationService recommendationService;

    /**
     * API để lấy các sản phẩm tương tự (gợi ý)
     * @param productId ID của sản phẩm đang xem
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Response> getRecommendations(@PathVariable("productId") Long productId) {

       Response recommendations = recommendationService.getSimilarProducts(productId);

        return ResponseEntity.ok(recommendations);
    }
}
