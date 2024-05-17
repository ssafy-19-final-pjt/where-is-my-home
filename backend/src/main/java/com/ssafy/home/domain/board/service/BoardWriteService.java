package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardWriteService {
    private final BoardRepository boardRepository;

    public void createBoard(Board board){
        boardRepository.save(board);
    }

    public void deleteBoard(Board board){
        boardRepository.delete(board);
    }
}
