package com.ssafy.home.domain.comment.controller;

import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="comment", description = "각 보드별 댓글")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary="게시글 댓글 전체 조회", description = "특정 게시글의 댓글을 전체 조회합니다")
    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentAll(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getCommentAll(boardId));
    }

    @Operation(summary="댓글 생성", description = "특정 게시글의 댓글을 생성합니다")
    @PostMapping("/{boardId}")
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal Long memberId,
                                              @PathVariable Long boardId,
                                              @RequestBody CommentRequestDto commentRequestDto){
        commentService.createComment(memberId, boardId, commentRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="댓글 수정", description = "자신이 작성한 댓글을 수정합니다")
    @PatchMapping("/{boardId}/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Long memberId,
                                              @PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentRequestUpdateDto commentRequestPatchDto){
        commentService.updateComment(memberId, boardId, commentId, commentRequestPatchDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary="댓글 삭제", description = "자신이 작성한 댓글을 삭제합니다")
    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal Long memberId, @PathVariable Long boardId, @PathVariable Long commentId){
        commentService.deleteComment(memberId, boardId, commentId);
        return ResponseEntity.ok().build();
    }
}
