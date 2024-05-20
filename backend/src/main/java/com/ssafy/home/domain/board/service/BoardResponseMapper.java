package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class BoardResponseMapper {
    public List<BoardResponseDto> toListBoardResponse(List<Board> boardList){
        return boardList.stream()
                .map(BoardResponseDto::from)
                .toList();
    }

    public BoardResponseDto toBoardResponse(Board board){
        return BoardResponseDto.from(board);
    }
}
