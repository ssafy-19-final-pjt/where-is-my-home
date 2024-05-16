package com.ssafy.home.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class MemberInfoResponseDto {
    @Schema(description = "회원 아이디", example = "1", required = true)
    private Long memberId;

    @Schema(description = "이메일", example = "test@gmail.com", required = true)
    private String email;

    @Schema(description = "회원 이름", example = "홍길동", required = true)
    private String memberName;

    @Schema(description = "프로필 이미지 경로", example = "http://k.kakaocdn.net/img_110x110.jpg", required = false)
    private String profile;
}
