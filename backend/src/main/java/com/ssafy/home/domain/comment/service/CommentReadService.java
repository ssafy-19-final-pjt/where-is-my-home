package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.comment.repository.CommentRepository;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentReadService {
    private final CommentRepository commentRepository;

    public List<Comment> getCommentFromBoardId(Long boardId){
        return commentRepository.findAllByBoardId(boardId);
    }

    public Comment getComment(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
                ()-> new BadRequestException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다 : " + commentId)
        );
    }
}
