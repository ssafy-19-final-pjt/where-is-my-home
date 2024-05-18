package com.ssafy.home.domain.wish.entity;

import com.ssafy.home.domain.homedeal.entity.HomeDeal;
import com.ssafy.home.entity.member.Member;
import jakarta.persistence.*;

@Entity
public class WishDeal {
    @Id
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="homedeal_id")
    private HomeDeal homeDeal;
}
