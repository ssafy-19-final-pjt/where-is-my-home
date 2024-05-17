package com.ssafy.home.domain.board.dto.response;

import com.ssafy.home.domain.board.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private String memberName;
    private Long id;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime createDate;

    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
                board.getMember().getName(),
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getHit(),
                board.getCreateDate());
    }
}
