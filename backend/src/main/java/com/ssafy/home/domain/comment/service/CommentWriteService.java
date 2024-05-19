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
        Comment comment = commentRequestDto.toEntity(member, board);
        commentRepository.save(comment);
        board.increaseHit();
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public void deleteByBoardId(Long boardId){
        commentRepository.deleteAllByBoardId(boardId);
    }
}
