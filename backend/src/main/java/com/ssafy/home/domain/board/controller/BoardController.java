package com.ssafy.home.domain.board.controller;

import com.ssafy.home.domain.board.dto.request.BoardCreateDto;
import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Board", description = "게시판 조회/생성/삭제")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @Operation(summary="게시판 게시글 전체 조회", description = "게시판 게시글을 전체 조회합니다.")
    @GetMapping("")
    ResponseEntity<List<BoardResponseDto>> getBoardAll() {
        return ResponseEntity.ok(boardService.getBoardAll());
    }

    @Operation(summary="게시판 게시글 조회", description = "게시판 특정 게시글을 조회합니다.")
    @GetMapping("/{boardId}")
    ResponseEntity<BoardResponseDto> getBoard(@AuthenticationPrincipal Long memberId,
                                              @PathVariable Long boardId) {
        log.info("boardId : " + boardId.toString());
        return ResponseEntity.ok(boardService.getBoard(memberId, boardId));
    }

    @Operation(summary="게시글 생성", description = "게시판에 게시글을 생성합니다.")
    @PostMapping("")
    ResponseEntity<Void> createBoard(@AuthenticationPrincipal Long memberId,
                                     @RequestBody BoardCreateDto boardCreateDto) {
        boardService.create(memberId, boardCreateDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 삭제", description = "게시판에 특정 게시글을 삭제합니다.")
    @DeleteMapping("/{boardId}")
    ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal Long memberId,
            @PathVariable Long boardId) {
        boardService.delete(memberId, boardId);
        return ResponseEntity.ok().build();
    }
}
