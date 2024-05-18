package com.ssafy.home.entity.member;

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
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private GeneralMember generalMember;

    @Column(name = "salt", nullable = false, length = 1000)
    private String salt;

    @Builder
    public MemberSecret(GeneralMember generalMember, String salt) {
        this.generalMember = generalMember;
        this.salt = salt;
    }
}
