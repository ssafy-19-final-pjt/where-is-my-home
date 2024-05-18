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
    @Column(name="aptCode")
    private Long aptCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="dongCode")
    private Dong dong;

    @Column(name="buildYear")
    private Integer buildYear;

    @Column(name="roadName")
    private String roadName;

    @Column(name="roadNameCode")
    private String roadNameCode;

    @Column(name="eubmyundongCode")
    private String eubmyundongCode;

    @Column(name="apartmentName")
    private String apartmentName;

    @Column(name="jibun")
    private String jibun;

    @Column(name="lng")
    private String lng;

    @Column(name="lat")
    private String lat;
}
