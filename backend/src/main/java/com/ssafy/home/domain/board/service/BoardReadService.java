package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardReadService {
    private final BoardRepository boardRepository;

    private final static int PAGE_SIZE = 50;

    public List<Board> getAllBoardList(Long cursor){
        return boardRepository.findNextPage(Objects.requireNonNullElse(cursor, 0L), PAGE_SIZE);
    }

    public Board getBoard(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(()->new BadRequestException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다 :" + boardId));
    }
}
