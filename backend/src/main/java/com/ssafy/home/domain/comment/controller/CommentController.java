package com.ssafy.home.domain.comment.controller;

import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="comment", description = "각 보드별 댓글")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentAll(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.getCommentAll(boardId));
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> createComment(@PathVariable Long boardId,
                                              @RequestBody CommentRequestDto commentRequestDto){
        commentService.createComment(boardId, commentRequestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{boardId}/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long boardId,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentRequestUpdateDto commentRequestPatchDto){
        commentService.updateComment(boardId, commentId, commentRequestPatchDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId){
        commentService.deleteComment(boardId, commentId);
        return ResponseEntity.ok().build();
    }
}
