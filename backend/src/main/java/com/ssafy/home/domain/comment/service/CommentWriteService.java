package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.comment.repository.CommentRepository;
import com.ssafy.home.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentWriteService {
    private final CommentRepository commentRepository;

    public void create(Member member, Board board, CommentRequestDto commentRequestDto) {
        commentRepository.save(commentRequestDto.toEntity(member, board));
    }

    public void delete(Member member, Board board, Comment comment) {
        commentRepository.delete(comment);
    }
}
