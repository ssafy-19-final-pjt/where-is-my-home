package com.ssafy.home.domain.home.controller;

import com.ssafy.home.domain.home.dto.request.HomeParam;
import com.ssafy.home.domain.home.dto.response.HomeResponseDto;
import com.ssafy.home.domain.home.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "아파트 정보 조회/상세 조회")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "아파트 정보 전체 조회", description = "저장된 아파트 정보를 전체 조회합니다")
    @GetMapping("")
    public ResponseEntity<List<HomeResponseDto>> getHomeList(@ModelAttribute HomeParam homeParam) {
        return ResponseEntity.ok(homeService.getHomeList(homeParam));
    }

    @Operation(summary = "아파트 정보 상세 조회", description = "저장된 아파트 정보중 확인하고자 하는 아파트의 정보를 상세 조회합니다")
    @GetMapping("/{homeId}")
    public ResponseEntity<HomeResponseDto> getHome(@PathVariable Long homeId) {
        return ResponseEntity.ok(homeService.getHome(homeId));
    }
}
