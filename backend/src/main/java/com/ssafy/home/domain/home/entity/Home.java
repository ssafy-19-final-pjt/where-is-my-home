package com.ssafy.home.domain.home.entity;

import com.ssafy.home.domain.dong.entity.Dong;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "houseinfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Home {
    @Id
    @Column(name="aptcode")
    private Long aptCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dongcode")
    private Dong dong;

    @Column(name="buildyear")
    private Integer buildYear;

    @Column(name="roadname")
    private String roadName;

    @Column(name="roadnamecode")
    private String roadNameCode;

    @Column(name="eubmyundongcode")
    private String eubmyundongCode;

    @Column(name="apartmentname")
    private String apartmentName;

    @Column(name="jibun")
    private String jibun;

    @Column(name="lng")
    private String lng;

    @Column(name="lat")
    private String lat;
}
