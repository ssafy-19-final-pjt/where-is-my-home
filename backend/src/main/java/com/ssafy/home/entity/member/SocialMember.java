package com.ssafy.home.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class SocialMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberType memberType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private SocialMember(MemberType memberType, Member member) {
        this.memberType = memberType;
        this.member = member;
    }
}
