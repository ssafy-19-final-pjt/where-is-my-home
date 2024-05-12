package com.ssafy.home.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponse {

    private String name;
    private String email;

    @Builder
    public ProfileResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
