package com.ssafy.home.domain.comment.controller;

import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.service.CommentService;
import com.ssafy.home.global.auth.dto.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="comment", description = "각 보드별 댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @Operation(summary="게시글 댓글 전체 조회", description = "특정 게시글의 댓글을 전체 조회합니다")
    @GetMapping(path="/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentResponseDto>> getCommentAll(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getCommentAll(boardId));
    }

    @Operation(summary="댓글 생성", description = "특정 게시글의 댓글을 생성합니다")
    @PostMapping(path="/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal MemberDto memberDto,
                                              @PathVariable Long boardId,
                                              @RequestBody CommentRequestDto commentRequestDto){
        commentService.createComment(memberDto, boardId, commentRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="댓글 수정", description = "자신이 작성한 댓글을 수정합니다")
    @PatchMapping("/{boardId}/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal MemberDto memberDto,
                                              @PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentRequestUpdateDto commentRequestPatchDto){
        commentService.updateComment(memberDto, boardId, commentId, commentRequestPatchDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="댓글 삭제", description = "자신이 작성한 댓글을 삭제합니다")
    @DeleteMapping(path="/{boardId}/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal MemberDto memberDto, @PathVariable Long boardId, @PathVariable Long commentId){
        commentService.deleteComment(memberDto, boardId, commentId);
        return ResponseEntity.ok().build();
    }
}
