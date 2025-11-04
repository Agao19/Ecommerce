package com.ZhongHou.Ecommerce.AI.VectorStoge;

import com.ZhongHou.Ecommerce.entity.Product;
import com.ZhongHou.Ecommerce.repository.ProductRepository;
import com.ZhongHou.Ecommerce.service.impl.ProductServiceImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductIngestionService implements ProductIngest {

    private final ProductRepository productRepository;


    @Override
    public void ingestAllProducts() {



    }

    @Override
    public void ingestProduct(Product product) {

    }

    @Override
    public void reIndexAllProducts() {

    }

    @Override
    public List<Product> getAllProductsAsEntities() {
        return productRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );
    }
}
