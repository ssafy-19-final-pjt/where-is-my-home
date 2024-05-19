package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentResponseMapper {
    public List<CommentResponseDto> toListCommentResponse(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponseDto::from)
                .toList();
    }
}
