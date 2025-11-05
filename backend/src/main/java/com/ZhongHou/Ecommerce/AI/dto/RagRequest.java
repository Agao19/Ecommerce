package com.ZhongHou.Ecommerce.AI.dto;

public record RagRequest(  String query,
                           String categoryId,
                           String priceRange) {
}
