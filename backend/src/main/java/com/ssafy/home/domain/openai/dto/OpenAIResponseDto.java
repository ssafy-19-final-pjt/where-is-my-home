package com.ssafy.home.domain.openai.dto;

import lombok.Getter;

@Getter
public class OpenAIResponseDto {
    private final String comment;

    public OpenAIResponseDto(String comment){
        this.comment = comment;
    }
}
