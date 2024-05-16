package com.ssafy.home.domain.user.controller;

import com.ssafy.home.domain.user.dto.request.LoginRequest;
import com.ssafy.home.domain.user.dto.request.RegisterRequest;
import com.ssafy.home.domain.user.dto.response.TokenResponse;
import com.ssafy.home.domain.user.service.MemberService;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){

        memberService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.OK).body("signup success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

        TokenResponse tokenResponse = memberService.login(loginRequest);

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, tokenResponse.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body("token success");
    }

    @GetMapping("/accessToken")
    public ResponseEntity<String> createAccessToken(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        String refreshToken = null;

        if (cookies != null) {
            refreshToken =  Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, memberService.reissue(refreshToken));

        return ResponseEntity.status(HttpStatus.OK).body("access token");
    }
}
