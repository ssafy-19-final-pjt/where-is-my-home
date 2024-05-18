package com.ssafy.home.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    private String profile;

    @Builder
    public MemberDto(Long id, String name, String email, String profile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }
}
