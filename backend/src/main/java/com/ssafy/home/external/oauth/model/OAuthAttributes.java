package com.ssafy.home.external.oauth.model;


import com.ssafy.home.domain.user.entity.Member;
import com.ssafy.home.domain.user.entity.MemberType;
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

    public Member toMemberEntity(MemberType memberType) {
        return Member.builder()
                .name(name)
                .email(email)
                .memberType(memberType)
                .profile(profile)
                .build();
    }

}
