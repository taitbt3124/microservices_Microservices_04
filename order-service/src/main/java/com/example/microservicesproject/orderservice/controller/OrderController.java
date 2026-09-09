package com.example.microservicesproject.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate loadBalancedRestTemplate;

    // --- BÀI TẬP 3: Tra cứu danh sách Instance qua DiscoveryClient thủ công ---
    @GetMapping("/getProduct-discovery/{id}")
    public ResponseEntity<?> getProductWithDiscoveryClient(@PathVariable Long id) {
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT-SERVICE");

        // Xử lý bài tập 3 yêu cầu: Trả về HTTP Status 503 nếu không có instance nào UP
        if (instances == null || instances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 503,
                    "error", "Service Unavailable",
                    "message", "No instances available for PRODUCT-SERVICE",
                    "path", "/api/v1/orders/getProduct-discovery/" + id
            ));
        }

        // Lấy Instance đầu tiên
        ServiceInstance instance = instances.get(0);
        String url = instance.getUri() + "/api/v1/products/" + id;

        Object response = restTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(response);
    }

    // --- BÀI TẬP 4: Cân bằng tải Client-side tự động dùng @LoadBalanced ---
    @GetMapping("/getProduct/{id}")
    public ResponseEntity<?> getProductWithLoadBalancer(@PathVariable Long id) {
        // Dùng Tên dịch vụ đăng ký trên Eureka thay vì IP/Port
        String url = "http://PRODUCT-SERVICE/api/v1/products/" + id;

        Object response = loadBalancedRestTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(response);
    }
}