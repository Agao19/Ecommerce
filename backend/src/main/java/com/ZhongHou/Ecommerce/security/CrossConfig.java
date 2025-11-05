package com.ZhongHou.Ecommerce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class CrossConfig implements WebMvcConfigurer{
    private final static Path IMAGE_DIRECTORY_BACKEND = Paths.get(System.getProperty("user.dir"), "product-images");

    @Bean
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Thêm các origin cụ thể (không dùng "*") khi dùng credentials
        configuration.setAllowedOrigins(List.of(
                "https://ecommerce.zhonghouaws.click",
                "https://exertive-barbar-interpenetratively.ngrok-free.dev",
                "http://localhost:4200"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        // Nếu cần wildcard (subdomains), dùng:
        // configuration.setAllowedOriginPatterns(List.of("https://*.example.com", "http://localhost:*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String imagePathUrl = IMAGE_DIRECTORY_BACKEND.toUri().toString();
        registry.addResourceHandler("/images/**")
                //.addResourceLocations("file:/D:/Ecommerce/product-images/")
                .addResourceLocations("imagePathUrl"); //Backend directory
    }
}
