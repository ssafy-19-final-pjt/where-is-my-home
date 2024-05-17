package com.ssafy.home.external.oauth.model;


import com.ssafy.home.entity.member.Member;
import com.ssafy.home.entity.member.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter @Builder
public class OAuthAttributes {

    private String name;
    private String email;
    private String profile;
    private MemberType memberType;

    public Member toMemberEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .profile(profile)
                .build();
    }

}
