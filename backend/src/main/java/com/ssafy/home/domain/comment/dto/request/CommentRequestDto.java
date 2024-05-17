package com.ssafy.home.domain.comment.dto.request;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {
    private String content;

    public Comment toEntity(Member member, Board board){
        return Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
    }
}
