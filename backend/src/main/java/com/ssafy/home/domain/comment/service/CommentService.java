package com.ssafy.home.domain.comment.service;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.service.BoardReadService;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.aop.distributed.DistributedLock;
import com.ssafy.home.global.aop.retry.Retry;
import com.ssafy.home.global.auth.dto.MemberDto;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentReadService commentReadService;
    private final CommentWriteService commentWriteService;
    private final BoardReadService boardReadService;
    private final MemberService memberService;
    private final CommentResponseMapper commentResponseMapper;

    public List<CommentResponseDto> getCommentAll(Long boardId) {
        Board board = boardReadService.getBoard(boardId);
        return commentResponseMapper.toListCommentResponse(board.getCommentList());
    }

    @Transactional
    public void createComment(MemberDto memberDto, Long boardId, CommentRequestDto commentRequestDto) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardReadService.getBoard(boardId);

        commentWriteService.create(member, board, commentRequestDto);
    }

    @Transactional
    public void createCommentWithPessimisticLock(MemberDto memberDto, Long boardId,
                                                 CommentRequestDto commentRequestDto) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardReadService.getboardWithPessimisticLock(boardId);

        commentWriteService.create(member, board, commentRequestDto);
    }

    @Transactional
    @Retry
    public void createCommentWithOptimisticLock(MemberDto memberDto, Long boardId,
                                                CommentRequestDto commentRequestDto) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardReadService.getBoardWithOptimisticLock(boardId);

        commentWriteService.create(member, board, commentRequestDto);
    }

    @Transactional
    @DistributedLock(key = "#boardId")
    public void createCommentWithDistributedLock(MemberDto memberDto, Long boardId,
                                                 CommentRequestDto commentRequestDto) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardReadService.getBoard(boardId);

        commentWriteService.create(member, board, commentRequestDto);
    }

    @Transactional
    public void updateComment(MemberDto memberDto, Long boardId, Long commentId,
                              CommentRequestUpdateDto commentRequestUpdateDto) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardReadService.getBoard(boardId);
        Comment comment = commentReadService.getComment(commentId);

        if (member.getId() != comment.getMember().getId()) {
            throw new BadRequestException(ErrorCode.CANNOT_UPDATE_COMMENT_YOU_NOT_CREATE);
        }

        if (board.getId() != comment.getBoard().getId()) {
            throw new BadRequestException(ErrorCode.BOARD_MISMATCH_FROM_BOARD, "게시글에 해당 댓글이 존재하지 않습니다.");
        }

        comment.updateContent(commentRequestUpdateDto.getContent());
    }

    @Transactional
    public void deleteComment(MemberDto memberDto, Long boardId, Long commentId) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardReadService.getBoard(boardId);
        Comment comment = commentReadService.getComment(commentId);

        if (member.getId() != comment.getMember().getId()) {
            throw new BadRequestException(ErrorCode.CANNOT_DELETE_COMMENT_YOU_NOT_CREATE);
        }

        if (board.getId() != comment.getBoard().getId()) {
            throw new BadRequestException(ErrorCode.BOARD_MISMATCH_FROM_BOARD, "게시글에 해당 댓글이 존재하지 않습니다.");
        }

        commentWriteService.delete(comment);
    }
}
