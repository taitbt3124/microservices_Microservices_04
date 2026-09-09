package com.example.microservicesproject.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // RestTemplate thường dùng cho DiscoveryClient (Bài 3)
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // RestTemplate có Cân bằng tải tự động (Bài 4)
    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }
}