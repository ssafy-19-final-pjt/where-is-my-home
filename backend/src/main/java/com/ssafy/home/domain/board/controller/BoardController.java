package com.ssafy.home.domain.board.controller;

import com.ssafy.home.domain.board.dto.request.BoardCreateDto;
import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Board", description = "게시판 조회/생성/삭제")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private BoardService boardService;

    @GetMapping("")
    ResponseEntity<List<BoardResponseDto>> getBoardAll() {
        return ResponseEntity.ok(boardService.getBoardAll());
    }

    @GetMapping("/{boardId}")
    ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
        log.info("boardId : " + boardId.toString());
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @PostMapping("")
    ResponseEntity<Void> createBoard(@RequestBody BoardCreateDto boardCreateDto) {
        boardService.create(boardCreateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("")
    ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.ok().build();
    }
}
