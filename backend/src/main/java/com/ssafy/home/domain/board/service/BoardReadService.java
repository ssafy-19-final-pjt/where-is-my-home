package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardReadService {
    private final BoardRepository boardRepository;

    private final static int PAGE_SIZE = 50;

    public List<Board> getAllBoardList(Long cursor){
        if(cursor == null){
            Pageable pageable = PageRequest.of(0,PAGE_SIZE);
            return boardRepository.findAll(pageable).getContent();
        } else {
            return boardRepository.findNextPage(cursor, PAGE_SIZE);
        }

    public Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다 :" + boardId));
    }

    public Board getboardWithPessimisticLock(Long boardId) {
        return boardRepository.findByIdPessimisticLock(boardId).orElseThrow();
    }

    public Board getBoardWithOptimisticLock(Long boardId) {
        return boardRepository.findByIdOptimisticLock(boardId).orElseThrow();
    }

    public Board getBoardWithDistributedLock(Long boardId) {
        return boardRepository.findByIdOptimisticLock(boardId).orElseThrow();
    }
}
