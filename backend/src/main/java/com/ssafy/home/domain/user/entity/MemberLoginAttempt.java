package com.ssafy.home.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member_login_attempt")
public class MemberLoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private long id;

    @OneToOne
    @JoinColumn(name="member_no")
    private Member member;

    @Column(name = "count", nullable = false, columnDefinition = "int default 0")
    private int count;

    @Column(name = "login_recent_attempt", nullable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date login_recent_attempt;

    @Builder
    public MemberLoginAttempt(Member member, int count, Date login_recent_attempt) {
        this.member = member;
        this.count = count;
        this.login_recent_attempt = login_recent_attempt;
    }
}
