package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardReadService {
    private final BoardRepository boardRepository;

    private final static int PAGE_SIZE = 50;

    public List<Board> getAllBoardList(int page){
        Pageable pageable = PageRequest.of(page-1,PAGE_SIZE);
        return boardRepository.findAll(pageable).getContent();
    }

    public Board getBoard(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(()->new BadRequestException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다 :" + boardId));
    }
}
