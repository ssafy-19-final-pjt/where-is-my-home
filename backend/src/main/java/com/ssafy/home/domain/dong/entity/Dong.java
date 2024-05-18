package com.ssafy.home.domain.dong.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="dongcode")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dong {
    @Id
    @Column(name = "dongcode")
    private String dongCode;

    @Column(name="sidoName", nullable=false, length=20)
    private String sidoName;

    @Column(name="gugunName", nullable = true)
    private String gugunName;

    @Column(name="dongName", nullable =true)
    private String dongName;
}
