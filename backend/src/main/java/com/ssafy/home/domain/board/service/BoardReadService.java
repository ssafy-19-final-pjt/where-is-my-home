package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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
                .orElseThrow();
    }
}
