package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.dto.request.BoardCreateDto;
import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponseDto> getBoardAll() {

        return null;
    }

    public BoardResponseDto getBoard(Long boardId) {

        return null;
    }

    @Transactional
    public void create(BoardCreateDto boardCreateDto) {
        
    }

    @Transactional
    public void delete(Long boardId) {

    }

}
