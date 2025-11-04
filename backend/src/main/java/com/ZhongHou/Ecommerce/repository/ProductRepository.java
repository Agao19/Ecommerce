package com.ZhongHou.Ecommerce.repository;

import com.ZhongHou.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingOrDescriptionContaining(String name, String description);

    @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.id DESC")
    List<Product> findAllWithCategory();

    Product getProductById(Long id);
}
