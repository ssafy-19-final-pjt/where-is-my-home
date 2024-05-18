package com.ssafy.home.domain.home.controller;

import com.ssafy.home.domain.home.dto.request.HomeParam;
import com.ssafy.home.domain.home.dto.response.HomeResponseDto;
import com.ssafy.home.domain.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping("")
    public ResponseEntity<List<HomeResponseDto>> getHomeList(@ModelAttribute HomeParam homeParam){
        return ResponseEntity.ok(homeService.getHomeList(homeParam));
    }

    @GetMapping("/{homeId}")
    public ResponseEntity<HomeResponseDto> getHome(@PathVariable Long homeId){
        return ResponseEntity.ok(homeService.getHome(homeId));
    }
}
