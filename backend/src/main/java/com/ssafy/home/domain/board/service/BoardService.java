package com.ssafy.home.domain.board.service;

import com.ssafy.home.domain.board.dto.request.BoardCreateDto;
import com.ssafy.home.domain.board.dto.response.BoardResponseDto;
import com.ssafy.home.domain.board.entity.Board;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardReadService boardReadService;
    private final BoardResponseMapper boardResponseMapper;
//    private final MemberService memberService;
    private final BoardWriteService boardWriteService;

    public List<BoardResponseDto> getBoardAll() {
        return boardResponseMapper.toListBoardResponse(boardReadService.getAllBoardList());
    }

    public BoardResponseDto getBoard(Long boardId) {
        return boardResponseMapper.toBoardResponse(boardReadService.getBoard(boardId));
    }

    @Transactional
    public void create(BoardCreateDto boardCreateDto) {
//        Member member = memberService.getMember();
//        Board board = boardCreateDto.toEntity(member);

    }

    @Transactional
    public void delete(Long boardId) {
        //Member member = memberService.getMember();
        Board board = boardReadService.getBoard(boardId);

//        if(board.getMember().getId()!=member.getId()){
//            throw new BadRequestException("올바른 사용자가 아닙니다.");
//        }

        boardWriteService.deleteBoard(board);
    }

}
