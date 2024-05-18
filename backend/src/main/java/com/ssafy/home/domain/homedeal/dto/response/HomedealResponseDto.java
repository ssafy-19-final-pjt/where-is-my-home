package com.ssafy.home.domain.homedeal.dto.response;

import com.ssafy.home.domain.homedeal.entity.HomeDeal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomedealResponseDto {
    private Long no;
    private String amount;
    private int year;
    private int month;
    private int day;
    private String area;
    private String floor;

    public static HomedealResponseDto from(HomeDeal homeDeal){
        return new HomedealResponseDto(homeDeal.getNo(), homeDeal.getDealAmount(), homeDeal.getDealYear(), homeDeal.getDealMonth(), homeDeal.getDealDay(), homeDeal.getArea(), homeDeal.getFloor());
    }
}
