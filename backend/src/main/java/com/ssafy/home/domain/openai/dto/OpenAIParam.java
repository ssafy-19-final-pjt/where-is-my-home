package com.ssafy.home.domain.openai.dto;

import lombok.Getter;

@Getter
public class OpenAIParam {
    String city;
    String dong;

    public OpenAIParam(String city, String dong){
        this.city = city;
        this.dong = dong;
    }
}
