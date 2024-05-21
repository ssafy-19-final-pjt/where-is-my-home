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

    @Column(name="sidoname", nullable=false, length=20)
    private String sidoName;

    @Column(name="gugunname", nullable = true)
    private String gugunName;

    @Column(name="dongname", nullable =true)
    private String dongName;
}
