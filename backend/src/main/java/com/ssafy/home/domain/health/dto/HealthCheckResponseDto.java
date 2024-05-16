package com.ssafy.home.domain.health.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor
public class HealthCheckResponseDto {

    @Schema(description = "서버 health 상태", example = "ok", required = true)
    private String health;

    @Schema(description = "현재 실행 중인 profile", example = "[dev]", required = true)
    private List<String> activeProfiles;

    @Builder
    HealthCheckResponseDto(String health, List<String> activeProfiles){
        this.health = health;
        this.activeProfiles = activeProfiles;
    }

}