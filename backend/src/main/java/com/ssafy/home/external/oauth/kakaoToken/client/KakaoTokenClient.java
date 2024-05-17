package com.ssafy.home.external.oauth.kakaoToken.client;

import com.ssafy.home.external.oauth.kakaoToken.dto.KakaoTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://kauth.kakao.com", name = "kakaoTokenClient")
public interface KakaoTokenClient {

    @PostMapping(value = "/oauth/token", consumes = "application/json")
    KakaoTokenDto.Response requestKakaoToken(@RequestHeader("Content-Type") String contentType,
                                             @SpringQueryMap KakaoTokenDto.Request request
    );

}
