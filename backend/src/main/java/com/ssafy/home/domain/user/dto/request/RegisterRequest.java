package com.ssafy.home.domain.user.dto.request;

import com.ssafy.home.domain.user.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .build();
    }
}
