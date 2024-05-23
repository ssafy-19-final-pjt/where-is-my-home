package com.ssafy.home.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentRequestUpdateDto {
    private String content;

    @Builder
    private CommentRequestUpdateDto(String content) {
        this.content = content;
    }
}
