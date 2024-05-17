package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.board.service.BoardReadService;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService{
    private final CommentReadService commentReadService;
    private final CommentWriteService commentWriteService;
    private final BoardReadService boardReadService;
    private final CommentResponseMapper commentResponseMapper;

    public List<CommentResponseDto> getCommentAll(Long boardId) {
        return commentResponseMapper.toListCommentResponse(commentReadService.getCommentFromBoardId(boardId));
    }

    public void createComment(Long boardId, CommentRequestDto commentRequestDto) {

    }

    @Transactional
    public void updateComment(Long boardId, Long commentId, CommentRequestUpdateDto commentRequestUpdateDto) {

    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId) {

    }
}
