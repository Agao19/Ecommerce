package com.ZhongHou.Ecommerce.AI.VectorStoge;

import com.ZhongHou.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductIngest {
    void ingestAllProducts();
    void ingestProduct(Product product);
    void reIndexAllProducts();
}
