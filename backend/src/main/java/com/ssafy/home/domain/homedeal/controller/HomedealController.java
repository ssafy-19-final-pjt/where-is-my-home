package com.ssafy.home.domain.homedeal.controller;

import com.ssafy.home.domain.homedeal.dto.response.HomedealResponseDto;
import com.ssafy.home.domain.homedeal.service.HomedealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/homedeal")
@RequiredArgsConstructor
public class HomedealController {
    private final HomedealService homedealService;

    @GetMapping("/{homeId}")
    public ResponseEntity<List<HomedealResponseDto>> getHomedealList(@PathVariable Long homeId){
        return ResponseEntity.ok(homedealService.getHomedealList(homeId));
    }

    @GetMapping("/{homeId}/{homedealId}")
    public ResponseEntity<HomedealResponseDto> getHomedeal(@PathVariable Long homeId, @PathVariable Long homedealId){
        return ResponseEntity.ok(homedealService.getHomedeal(homeId, homedealId));
    }
}
