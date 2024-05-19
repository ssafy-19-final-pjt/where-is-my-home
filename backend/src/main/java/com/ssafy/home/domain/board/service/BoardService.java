package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.dto.request.BoardCreateDto;
import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.dto.MemberDto;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardReadService boardReadService;
    private final BoardResponseMapper boardResponseMapper;
    private final MemberService memberService;
    private final BoardWriteService boardWriteService;

    public List<BoardResponseDto> getBoardAll() {
        return boardResponseMapper.toListBoardResponse(boardReadService.getAllBoardList());
    }

    public BoardResponseDto getBoard(Long boardId) {
        return boardResponseMapper.toBoardResponse(boardReadService.getBoard(boardId));
    }

    @Transactional
    public void create(MemberDto memberDto, BoardCreateDto boardCreateDto) {
        Member member = memberService.getMemberById(memberDto.getId());
        Board board = boardCreateDto.toEntity(member);

        boardWriteService.createBoard(board);
    }

    @Transactional
    public void delete(MemberDto memberDto, Long boardId) {
        Board board = boardReadService.getBoard(boardId);

        if(board.getMember().getId() != memberDto.getId()){
            throw new BadRequestException(ErrorCode.CANNOT_DELETE_BOARD_YOU_NOT_CREATE);
        }

        boardWriteService.deleteBoard(board);
    }

}
