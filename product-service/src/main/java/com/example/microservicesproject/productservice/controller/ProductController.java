package com.example.microservicesproject.productservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
        System.out.println("-> [PRODUCT-SERVICE] Handled request on Port: " + serverPort);
        return Map.of(
                "id", id,
                "name", "Iphone 17",
                "price", 123456.0,
                "stockQuantity", 50,
                "servedByPort", serverPort
        );
    }
}