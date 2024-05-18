package com.ssafy.home.domain.wish.controller;

import com.ssafy.home.domain.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;

    @GetMapping("/deal")
    public ResponseEntity<List<WishHomedealResponseDto>> getWishDeal(@AuthenticationPrincipal MemberDto user){

    }
}
