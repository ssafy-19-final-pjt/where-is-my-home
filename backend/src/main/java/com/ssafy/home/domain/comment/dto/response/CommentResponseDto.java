package com.ssafy.home.domain.comment.dto.response;

import com.ssafy.home.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long memberId;
    private String content;
    private LocalDateTime createdDate;

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getContent(),
                comment.getCreateDate());
    }
}
