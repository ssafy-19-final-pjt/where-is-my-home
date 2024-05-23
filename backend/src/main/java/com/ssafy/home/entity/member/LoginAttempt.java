package com.ssafy.home.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "count")
    @ColumnDefault("0")
    private int count;

    @LastModifiedDate
    @Column(name = "login_recent_attemp", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime loginRecentAttemp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public LoginAttempt(Member member) {
        this.member = member;
    }

    public void updateCount(){
        this.count = this.count + 1;
    }

    public void initCount(){
        this.count = 0;
    }
}
