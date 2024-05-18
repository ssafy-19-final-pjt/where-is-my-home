package com.ssafy.home.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordResponse {
    private String receiveAddress;
    private String mailTitle;
    private String mailContent;

    @Builder
    public FindPasswordResponse(String receiveAddress, String mailTitle, String mailContent) {
        this.receiveAddress = receiveAddress;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
    }
}
