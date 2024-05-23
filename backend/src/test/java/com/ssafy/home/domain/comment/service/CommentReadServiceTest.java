package com.ssafy.home.domain.comment.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.comment.repository.CommentRepository;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import groovy.util.logging.Slf4j;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
class CommentReadServiceTest extends TestConfig {
    private final CommentReadService commentReadService;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private Member member;
    private Board board;
    private Comment comment;

    @Autowired
    public CommentReadServiceTest(CommentReadService commentReadService, MemberRepository memberRepository,
                                  BoardRepository boardRepository, CommentRepository commentRepository) {
        this.commentReadService = commentReadService;
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
        comment = commentRepository.save(Comment.builder()
                .content("test")
                .member(member)
                .board(board)
                .build());
        commentRepository.save(Comment.builder()
                .content("test2")
                .member(member)
                .board(board)
                .build());
    }

    @Nested
    @DisplayName("댓글 읽기 기능 테스트")
    class CommentRead {
        @Test
        void 성공_게시판_ID를_입력_시_게시판에_작성된_댓글을_가져온다() {
            //given
            //when
            List<Comment> result = commentReadService.getCommentFromBoardId(board.getId());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                        softAssertions.assertThat(result).isNotNull()
                                .hasSize(2)
                                .allMatch(comment -> comment.getMember().getId().equals(member.getId()))
                                .allMatch(comment -> comment.getContent().startsWith("test"));
                    }
            );
        }

        @Test
        void 성공_댓글_ID를_입력_시_작성된_댓글을_가져온다() {
            //given
            //when
            Comment result = commentReadService.getComment(comment.getId());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.getBoard().getId()).isEqualTo(board.getId());
                softAssertions.assertThat(result.getMember().getId()).isEqualTo(member.getId());
                softAssertions.assertThat(result.getContent()).isEqualTo(comment.getContent());
            });
        }

        @Test
        void 실패_댓글_ID가_존재하지_않는_경우_예외가_발생한다() {
            //given
            Long noneExistCommentID = 0L;

            //when then
            assertThatThrownBy(() -> commentReadService.getComment(noneExistCommentID))
                    .isInstanceOf(BadRequestException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
        }
    }
}