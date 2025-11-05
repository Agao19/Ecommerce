package com.ZhongHou.Ecommerce.AI.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
@ConditionalOnProperty(prefix = "app.rag", name = "enabled", havingValue = "true",matchIfMissing=true)
public class VectorStoreConfig {

    /**
     * JedisPooled cho VectorStore (port 6380 - Redis Stack)
     * Khác với Redis cache (port 6379)
     */
    @Bean(name = "vectorStoreJedisPooled")
    @ConditionalOnMissingBean(name = "vectorStoreJedisPooled")
    public JedisPooled vectorStoreJedisPooled() {
        return new JedisPooled("localhost", 6380);
    }

    /**
     * VectorStore bean mặc định (index mặc định: spring-ai-index, prefix: spring-ai:docs).
     */
    @Bean
    @ConditionalOnMissingBean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        JedisPooled jedisPooled = vectorStoreJedisPooled();
        return RedisVectorStore
                .builder(jedisPooled, embeddingModel)
                .indexName("spring-ai-index")
                
                //Metada Filter by categoryId and priceRange
                .metadataFields(
                        RedisVectorStore.MetadataField.tag("categoryId"),
                        RedisVectorStore.MetadataField.tag("priceRange")
                )

                //key prefix for raw documents (content + metadata)
                .prefix("spring-ai:docs")

                .initializeSchema(true)
                .build();
    }
}

