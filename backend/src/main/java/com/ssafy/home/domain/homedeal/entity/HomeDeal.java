package com.ssafy.home.domain.homedeal.entity;

import com.ssafy.home.domain.home.entity.Home;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="housedeal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeDeal {
    @Id
    @Column(name="no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="aptcode")
    private Home home;

    @Column(name="dealamount")
    private String dealAmount;

    @Column(name="dealyear")
    private Integer dealYear;

    @Column(name="dealmonth")
    private Integer dealMonth;

    @Column(name="dealday")
    private Integer dealDay;

    @Column(name="area")
    private String area;

    @Column(name="floor")
    private String floor;

    @Column(name="canceldealtype")
    private String cancelDealType;
}
