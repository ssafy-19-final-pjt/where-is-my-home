package com.ssafy.home.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GeneralMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_enc_password", nullable = false, length = 256)
    private String userEncPassword;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToOne(mappedBy = "generalMember")
    MemberSecret memberSecret;

    @Builder
    private GeneralMember(String userEncPassword, Member member, MemberSecret memberSecret) {
        this.userEncPassword = userEncPassword;
        this.member = member;
        this.memberSecret = memberSecret;
    }
}
