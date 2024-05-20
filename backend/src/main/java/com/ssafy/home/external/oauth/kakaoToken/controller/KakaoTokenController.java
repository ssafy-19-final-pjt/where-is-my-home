package com.ssafy.home.external.oauth.kakaoToken.controller;


import com.ssafy.home.external.oauth.kakaoToken.client.KakaoTokenClient;
import com.ssafy.home.external.oauth.kakaoToken.dto.KakaoTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/kakao/login")
    public String login() {
        return "loginForm";
    }

    @Tag(name = "authentication")
    @Operation(summary = "소셜 로그인 token 생성 api", description = "소셜 로그인 token 생성 api")
    @GetMapping(path = "/oauth/kakao/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<KakaoTokenDto.Response> loginCallback(@RequestParam("code") String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri("http://localhost:8080/oauth/kakao/callback")
                .build();
        KakaoTokenDto.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(kakaoToken);
    }

}
