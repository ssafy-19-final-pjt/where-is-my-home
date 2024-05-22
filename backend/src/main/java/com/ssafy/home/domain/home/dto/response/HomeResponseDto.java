package com.ssafy.home.domain.home.dto.response;

import com.ssafy.home.domain.home.entity.Home;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeResponseDto {
    private Long aptCode;
    private int buildYear;
    private String roadName;
    private String aptName;
    private String jibun;

    public static HomeResponseDto from(Home home){
        return new HomeResponseDto(home.getAptCode(), home.getBuildYear(), home.getRoadName(), home.getApartmentName(), home.getJibun());
    }
}
