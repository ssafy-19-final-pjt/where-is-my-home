package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardReadService {
    private final BoardRepository boardRepository;

    public List<Board> getAllBoardList(){
        return boardRepository.findAll();
    }

    public Board getBoard(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(()->new BadRequestException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다 :" + boardId));
    }

    public Board getboardWithPessimisticLock(Long boardId) {
        return boardRepository.findByIdPessimisticLock(boardId).orElseThrow();
    }
}
