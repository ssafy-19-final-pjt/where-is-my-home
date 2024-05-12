package com.ssafy.home.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_secret")
public class MemberSecret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private long id;

    @OneToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @Column(name = "salt", nullable = false, length = 1000)
    private String salt;

    @Builder
    public MemberSecret(Member member, String salt) {
        this.member = member;
        this.salt = salt;
    }
}
