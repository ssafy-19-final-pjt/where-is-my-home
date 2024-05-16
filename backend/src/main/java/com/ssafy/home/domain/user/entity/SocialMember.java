package com.ssafy.home.domain.user.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class SocialMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "oauth_server")
    private String oauthServer;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;
}
