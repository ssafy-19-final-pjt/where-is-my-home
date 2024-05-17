package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.comment.repository.CommentRepository;
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

    public Comment getCommentFromCommentId(Long commentId){
        //TODO : 오류처리
        return commentRepository.findById(commentId).orElseThrow();
    }
}
