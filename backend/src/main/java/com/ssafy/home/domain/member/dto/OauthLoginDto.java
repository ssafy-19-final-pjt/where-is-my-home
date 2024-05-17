package com.ssafy.home.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.home.global.auth.jwt.dto.JwtTokenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

public class OauthLoginDto {

    @Getter
    @Setter
    public static class Request {
        @Schema(description = "소셜 로그인 회원 타입", example = "KAKAO", required = true)
        private String memberType;
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "grantType", example = "Bearer", required = true)
        private String grantType;

        @Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJpYXQiOjE2NTg0ODAyOTYsImV4cCI6MTY1ODQ4MTE5NiwibWVtYmVySWQiOjEsInJvbGUiOiJBRE1JTiJ9.qr5fOs9NIO5UYJzqgisESOXorASLphj04uyjF1Breolj4cou_k6py0egF8f3OxWjQXps3on7Ko3jwIaL_2voRg", required = true)
        private String accessToken;

        @Schema(description = "access token 만료 시간", example = "2022-07-22 18:13:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDate accessTokenExpireTime;

        @Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSRUZSRVNIIiwiaWF0aASDgwMjk3LCJleHAiOjE2NTk2ODk4OTYsIm1lbWJlcklkIjoxfQ.hxgq_DIU554lUnUCSAGTYOiaXLXwgpyIM2h8a5de3ALEOY-IokElS6VbMmVTKlpRaLfEzzcr3FkUDrNisRt-bA", required = true)
        private String refreshToken;

        @Schema(description = "refresh token 만료 시간", example = "2022-08-05 18:13:16", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDate refreshTokenExpireTime;

        public static Response of(JwtTokenDto jwtTokenDto) {
            return Response.builder()
                    .grantType(jwtTokenDto.getGrantType())
                    .accessToken(jwtTokenDto.getAccessToken())
                    .accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
                    .refreshToken(jwtTokenDto.getRefreshToken())
                    .refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
                    .build();
        }

    }

}