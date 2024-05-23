package com.ssafy.home.domain.homedeal.controller;

import com.ssafy.home.domain.homedeal.dto.response.HomedealResponseDto;
import com.ssafy.home.domain.homedeal.service.HomedealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HomeDeal", description = "아파트 거래 정보 조회/상세 조회")
@RestController
@RequestMapping("/homedeal")
@RequiredArgsConstructor
public class HomedealController {
    private final HomedealService homedealService;

    @Operation(summary = "아파트 거래 정보 전체 조회", description = "특정 아파트에 대한 거래 정보를 전체 조회합니다")
    @GetMapping("/{homeId}")
    public ResponseEntity<List<HomedealResponseDto>> getHomedealList(@PathVariable Long homeId) {
        return ResponseEntity.ok(homedealService.getHomedealList(homeId));
    }

    @Operation(summary = "아파트 거래 정보 상세 조회", description = "해당 아파트의 거래 상세 정보를 조회합니다.")
    @GetMapping("/{homeId}/{homedealId}")
    public ResponseEntity<HomedealResponseDto> getHomedeal(@PathVariable Long homeId, @PathVariable Long homedealId) {
        return ResponseEntity.ok(homedealService.getHomedeal(homeId, homedealId));
    }
}
