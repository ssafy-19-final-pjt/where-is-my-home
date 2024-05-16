package com.ssafy.home.domain.feigntest.client;

import com.ssafy.home.domain.health.dto.HealthCheckResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url="localhost:8080", name = "helloClient")
public interface HelloClient {

    @GetMapping(value = "/health", consumes = "application/json")
    HealthCheckResponseDto healthCheck();
}
