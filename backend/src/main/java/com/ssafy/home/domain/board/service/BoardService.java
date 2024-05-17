package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.dto.request.BoardCreateDto;
import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
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

    public BoardResponseDto getBoard(Long memberId, Long boardId) {
        return boardResponseMapper.toBoardResponse(boardReadService.getBoard(boardId));
    }

    @Transactional
    public void create(Long memberId, BoardCreateDto boardCreateDto) {
        Member member = memberService.getMemberById(memberId);
        Board board = boardCreateDto.toEntity(member);

        boardWriteService.createBoard(board);
    }

    @Transactional
    public void delete(Long memberId, Long boardId) {
        Member member = memberService.getMemberById(memberId);
        Board board = boardReadService.getBoard(boardId);

        //TODO : 리팩토링 필요
        if(board.getMember().getId()!=member.getId()){
//            throw new BadRequestException("올바른 사용자가 아닙니다.");
            return;
        }

        boardWriteService.deleteBoard(board);
    }

}
