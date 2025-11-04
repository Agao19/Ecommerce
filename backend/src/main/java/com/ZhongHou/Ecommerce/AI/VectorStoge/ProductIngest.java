package com.ZhongHou.Ecommerce.AI.VectorStoge;

import com.ZhongHou.Ecommerce.entity.Product;

import java.util.List;

public interface ProductIngest {
    void ingestAllProducts();
    void ingestProduct(Product product);
    void reIndexAllProducts();

    List<Product> getAllProductsAsEntities();

}
