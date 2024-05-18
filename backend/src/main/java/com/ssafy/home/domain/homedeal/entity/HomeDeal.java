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
    @JoinColumn(name="aptCode")
    private Home home;

    @Column(name="dealAmount")
    private String dealAmount;

    @Column(name="dealYear")
    private Integer dealYear;

    @Column(name="dealMonth")
    private Integer dealMonth;

    @Column(name="dealDay")
    private Integer dealDay;

    @Column(name="area")
    private String area;

    @Column(name="floor")
    private String floor;

    @Column(name="cancelDealType")
    private String cancelDealType;
}
