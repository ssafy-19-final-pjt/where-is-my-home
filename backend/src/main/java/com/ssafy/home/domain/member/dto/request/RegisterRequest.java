package com.ssafy.home.domain.member.dto.request;

import com.ssafy.home.entity.member.Member;
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
