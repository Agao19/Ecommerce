package com.ZhongHou.Ecommerce.AI.VectorStoge;

import com.ZhongHou.Ecommerce.entity.Product;
import com.ZhongHou.Ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductIngestionService implements ProductIngest {

    private final ProductRepository productRepository;
    private final VectorStore vectorStore;


    @Override
    public void ingestAllProducts() {
        var productsList = productRepository.findAllWithCategory();

        //Convert to Document
        List<Document> documents = productsList.stream()
                .map(product -> {
                    String content = buildContent(product);
                    Map<String, Object> metadata = buildMetadata(product);
                    return new Document(content, metadata);
                })
                .toList();

        //Chunk description
        TokenTextSplitter splitter = new TokenTextSplitter(512,64,5,1000,true);
        List<Document> chunks = splitter.apply(documents);
        log.info("Created {} document chunks from {} products", chunks.size(), productsList.size());

        //add vectorStore 
        vectorStore.add(chunks);
        log.info("Successfully ingested {} chunks into VectorStore", chunks.size());
    }

    @Override
    public void ingestProduct(Product product) {
        log.info("Ingesting single product: {}", product.getId());
        
        String content = buildContent(product);
        Map<String, Object> metadata = buildMetadata(product);
        Document document = new Document(content, metadata);

        TokenTextSplitter splitter = new TokenTextSplitter(512,64,5,1000,true);
        List<Document> chunks = splitter.apply(List.of(document));
        
        vectorStore.add(chunks);
        log.info("Successfully ingested product: {}", product.getId());
    }

    @Override
    public void reIndexAllProducts() {
        log.info("Re-indexing all products...");
        ingestAllProducts();
    }


    private String buildContent(Product product) {
        StringBuilder content = new StringBuilder();

        content.append("Product: ").append(product.getName()).append("\n");

        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            content.append("Description: ").append(product.getDescription()).append("\n");
        }

        if (product.getCategory() != null) {
            content.append("Category: ").append(product.getCategory().getName()).append("\n");
        }

        if (product.getPrice() != null) {
            content.append("Price: $").append(product.getPrice()).append("\n");
        }

        return content.toString();
    }

    private Map<String, Object> buildMetadata(Product product) {
        Map<String, Object> metadata = new HashMap<>();

        // Product info
        metadata.put("productId", product.getId().toString());
        metadata.put("productName", product.getName());

        // Category info
        if (product.getCategory() != null) {
            metadata.put("categoryId", product.getCategory().getId().toString());
            metadata.put("categoryName", product.getCategory().getName());
        }

        // Price info
        if (product.getPrice() != null) {
            metadata.put("price", product.getPrice().toString());
            metadata.put("priceRange", getPriceRange(product.getPrice()));
        }

        // Image
        if (product.getImageUrl() != null) {
            metadata.put("imageUrl", product.getImageUrl());
        }

        // Created date
        if (product.getCreatedAt() != null) {
            metadata.put("createdAt", product.getCreatedAt().toString());
        }

        return metadata;
    }

    private String getPriceRange(BigDecimal price) {
        if (price == null) return "unknown";

        double priceValue = price.doubleValue();
        if (priceValue < 100) {
            return "low";
        } else if (priceValue < 500) {
            return "medium";
        } else {
            return "high";
        }
    }


}
