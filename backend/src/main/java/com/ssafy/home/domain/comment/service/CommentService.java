package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.service.BoardReadService;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
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
    private final MemberService memberService;
    private final CommentResponseMapper commentResponseMapper;

    public List<CommentResponseDto> getCommentAll(Long boardId) {
        return commentResponseMapper.toListCommentResponse(commentReadService.getCommentFromBoardId(boardId));
    }

    @Transactional
    public void createComment(Long memberId, Long boardId, CommentRequestDto commentRequestDto) {
        Member member = memberService.getMemberById(memberId);
        Board board = boardReadService.getBoard(boardId);

        commentWriteService.create(member, board, commentRequestDto);
    }

    @Transactional
    public void updateComment(Long memberId, Long boardId, Long commentId, CommentRequestUpdateDto commentRequestUpdateDto) {
        Member member = memberService.getMemberById(memberId);
        Board board = boardReadService.getBoard(boardId);
        Comment comment = commentReadService.getComment(commentId);

        if(member.getId()!= comment.getMember().getId()){
            //TODO : 오류처리
//            throw new BadRequestException();
            return;
        }
        comment.updateContent(commentRequestUpdateDto.getContent());
    }

    @Transactional
    public void deleteComment(Long memberId, Long boardId, Long commentId) {
        Member member = memberService.getMemberById(memberId);
        Board board = boardReadService.getBoard(boardId);
        Comment comment = commentReadService.getComment(commentId);
        
        if(member.getId()!= comment.getMember().getId()){
            //TODO : 오류처리
            return;
        }
        commentWriteService.delete(member, board, comment);
    }
}
