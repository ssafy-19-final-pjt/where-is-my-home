package com.ssafy.home.domain.comment.service;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.comment.repository.CommentRepository;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.entity.member.Member;
import groovy.util.logging.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
class CommentWriteServiceTest extends TestConfig {
    private final CommentWriteService commentWriteService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private Member member;
    private Board board;

    @Autowired
    public CommentWriteServiceTest(CommentWriteService commentWriteService, MemberRepository memberRepository,
                                   BoardRepository boardRepository, CommentRepository commentRepository) {
        this.commentWriteService = commentWriteService;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    @BeforeEach
    void before() {
        member = memberRepository.findById(11L).orElseThrow(IllegalStateException::new);
        board = boardRepository.save(Board.builder()
                .content("test")
                .member(member)
                .title("testTitled")
                .build());
    }

    @Nested
    @DisplayName("댓글 생성/삭제/전체 삭제 테스트")
    class CommentWrite {
        @Test
        void 성공_댓글을_생성한다() {
            // given
            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("댓글 내용").build();

            // when
            commentWriteService.create(member, board, commentRequestDto);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(commentRepository.findAllByBoardId(board.getId())).hasSize(1);
                Comment comment = commentRepository.findAllByBoardId(board.getId()).get(0);

                softAssertions.assertThat(comment.getContent()).isEqualTo("댓글 내용");
                softAssertions.assertThat(comment.getMember().getId()).isEqualTo(member.getId());
                softAssertions.assertThat(comment.getBoard().getId()).isEqualTo(board.getId());
            });
        }

        @Test
        void 성공_댓글을_삭제한다() {
            // given
            Comment comment = commentRepository.save(Comment.builder()
                    .content("삭제될 댓글")
                    .member(member)
                    .board(board)
                    .build());

            // when
            commentWriteService.delete(comment);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(commentRepository.findById(comment.getId())).isEmpty();
            });
        }

        @Test
        void 성공_게시판에_연관되어있는_모든_댓글을_삭제한다() {
            // given
            Board newBoard = boardRepository.save(Board.builder()
                    .content("test")
                    .member(member)
                    .title("testTitled")
                    .build());

            commentRepository.save(Comment.builder()
                    .content("삭제될 댓글 1")
                    .member(member)
                    .board(newBoard)
                    .build());
            commentRepository.save(Comment.builder()
                    .content("삭제될 댓글 2")
                    .member(member)
                    .board(newBoard)
                    .build());

            // when
            commentWriteService.deleteByBoardId(newBoard.getId());

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(commentRepository.findAllByBoardId(newBoard.getId())).isEmpty();
            });
        }
    }
}