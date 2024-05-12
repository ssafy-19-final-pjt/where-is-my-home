package com.ssafy.home.domain.user.dto.request;

import com.ssafy.home.domain.user.domain.Member;
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
                .userEncPassword(password)
                .build();
    }

    public void createPassword(String password){
        this.password = password;
    }
}
