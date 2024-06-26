package com.ssafy.home.domain.board.dto.request;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateDto {
    private String title;
    private String content;

    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .content(content)
                .hit(0)
                .member(member)
                .build();
    }
}
