package com.ssafy.home.domain.comment.service;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.domain.comment.dto.request.CommentRequestUpdateDto;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.domain.comment.repository.CommentRepository;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.dto.MemberDto;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BadRequestException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends TestConfig {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceTest.class);
    private final CommentService commentService;
    private final MemberService memberService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private Member member;
    private Board board;
    private MemberDto memberDto;
    private Long wrongId;

    @Autowired
    public CommentServiceTest(CommentService commentService, MemberService memberService,
                              BoardRepository boardRepository, CommentRepository commentRepository,
                              MemberRepository memberRepository) {
        this.commentService = commentService;
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    @BeforeEach
    void before() {
        member = memberService.getMemberById(11L);
        board = boardRepository.save(Board.builder()
                .content("test")
                .member(member)
                .title("testTitled")
                .build());
        memberDto = MemberDto.builder().id(member.getId()).name(member.getName()).build();
        wrongId = 0L;
    }

    @Nested
    @DisplayName("댓글 성공/수정/삭제 테스트")
    class CommentFunction {
        @Test
        @Transactional
        void 성공_게시글의_모든_댓글을_확인한다() {
            //given
            Comment comment = commentRepository.save(Comment.builder()
                    .content("댓글 1")
                    .member(member)
                    .board(board)
                    .build());

            //when
            List<CommentResponseDto> commentList = commentService.getCommentAll(board.getId());

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(commentList).isNotNull()
                        .hasSize(1);
                softAssertions.assertThat(commentList.get(0).getContent()).isEqualTo("댓글 1");
                softAssertions.assertThat(commentList.get(0).getMemberId()).isEqualTo(member.getId());
            });
        }

        @Nested
        @DisplayName("댓글 수정")
        class CommentUpdate {
            @Test
            void 성공_자신이_작성한_댓글을_수정할_수_있다() {
                //given
                Comment comment = commentRepository.save(Comment.builder()
                        .content("댓글 1")
                        .member(member)
                        .board(board)
                        .build());

                CommentRequestUpdateDto commentRequestUpdateDto = CommentRequestUpdateDto.builder().content("댓글 수정")
                        .build();

                //when
                commentService.updateComment(memberDto, board.getId(), comment.getId(), commentRequestUpdateDto);

                Comment result = commentRepository.findById(comment.getId()).orElseThrow();
                //then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(result).isNotNull();
                    softAssertions.assertThat(result.getContent()).isEqualTo("댓글 수정");
                });
            }

            @Test
            void 실패_자신이_작성하지_않은_댓글_수정_요청시_예외가_발생한다() {
                Comment comment = commentRepository.save(Comment.builder()
                        .content("댓글 1")
                        .member(member)
                        .board(board)
                        .build());

                CommentRequestUpdateDto commentRequestUpdateDto = CommentRequestUpdateDto.builder().content("댓글 수정")
                        .build();

                Member testMember = memberRepository.save(
                        Member.builder().name("1").email("1").profile("1").refreshToken("1").build());

                MemberDto testMemberDto = MemberDto.builder().id(testMember.getId()).name(testMember.getName()).build();

                //when then
                assertThatThrownBy(() -> commentService.updateComment(testMemberDto, board.getId(), comment.getId(),
                        commentRequestUpdateDto))
                        .isInstanceOf(BadRequestException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.CANNOT_UPDATE_COMMENT_YOU_NOT_CREATE);
            }

            @Test
            void 실패_수정하고자하는_게시글의_댓글이_연관되어_있지_않은경우_예외가_발생한다() {
                Comment comment = commentRepository.save(Comment.builder()
                        .content("댓글 1")
                        .member(member)
                        .board(board)
                        .build());

                CommentRequestUpdateDto commentRequestUpdateDto = CommentRequestUpdateDto.builder().content("댓글 수정")
                        .build();

                //when then
                assertThatThrownBy(() -> commentService.updateComment(memberDto, wrongId, comment.getId(),
                        commentRequestUpdateDto))
                        .isInstanceOf(BadRequestException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.BOARD_NOT_FOUND);
            }
        }

        @Nested
        @DisplayName("댓글 삭제")
        class CommentDelete {
            @Test
            void 성공_자신이_작성한_댓글을_삭제할_수_있다() {
                //given
                Comment comment = commentRepository.save(Comment.builder()
                        .content("댓글 1")
                        .member(member)
                        .board(board)
                        .build());

                //when
                commentService.deleteComment(memberDto, board.getId(), comment.getId());

                //then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(commentRepository.findById(comment.getId())).isEmpty();
                });
            }

            @Test
            void 실패_자신이_작성하지_않은_댓글_삭제_요청시_예외가_발생한다() {
                Comment comment = commentRepository.save(Comment.builder()
                        .content("댓글 1")
                        .member(member)
                        .board(board)
                        .build());

                CommentRequestUpdateDto commentRequestUpdateDto = CommentRequestUpdateDto.builder().content("댓글 수정")
                        .build();

                Member testMember = memberRepository.save(
                        Member.builder().name("1").email("1").profile("1").refreshToken("1").build());

                MemberDto testMemberDto = MemberDto.builder().id(testMember.getId()).name(testMember.getName()).build();

                //when then
                assertThatThrownBy(() -> commentService.deleteComment(testMemberDto, board.getId(), comment.getId()))
                        .isInstanceOf(BadRequestException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.CANNOT_DELETE_COMMENT_YOU_NOT_CREATE);
            }

            @Test
            void 실패_삭제하고자하는_게시글의_댓글이_연관되어_있지_않은경우_예외가_발생한다() {
                Comment comment = commentRepository.save(Comment.builder()
                        .content("댓글 1")
                        .member(member)
                        .board(board)
                        .build());

                CommentRequestUpdateDto commentRequestUpdateDto = CommentRequestUpdateDto.builder().content("댓글 수정")
                        .build();

                //when then
                assertThatThrownBy(() -> commentService.deleteComment(memberDto, wrongId, comment.getId()))
                        .isInstanceOf(BadRequestException.class)
                        .extracting("errorCode")
                        .isEqualTo(ErrorCode.BOARD_NOT_FOUND);
            }
        }

    }


//    @Nested
//    @DisplayName("댓글 동시성 처리 관련 테스트")
//    class CommentCouncurrency {
//        @Test
//        @Transactional
//        void 성공_조회수_추가() {
//            //given
//            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();
//
//            //when
//            for (int i = 0; i < 100; i++) {
//                commentService.createComment(memberDto, board.getId(), commentRequestDto);
//            }
//
//            //then
//            assertThat(board.getHit()).isEqualTo(100);
//        }
//
//        @Test
//        void 에러_기본_코드는_동시성_테스트시_성공하지_않는_트랜잭션이_발생한다() throws InterruptedException {
//            int threadCount = 100;
//
//            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();
//
//            ExecutorService executorService = Executors.newFixedThreadPool(20);
//            CountDownLatch latch = new CountDownLatch(threadCount);
//
//            AtomicInteger hitCount = new AtomicInteger(0);
//            AtomicInteger missCount = new AtomicInteger(0);
//
//            for (int i = 0; i < threadCount; i++) {
//                executorService.execute(() -> {
//                    try {
//                        commentService.createComment(memberDto, board.getId(), commentRequestDto);
//                        hitCount.incrementAndGet();
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                        missCount.incrementAndGet();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//            latch.await();
//
//            System.out.println("hitCount = " + hitCount);
//            System.out.println("missCount = " + missCount);
//
//            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();
//
//            assertThat(newBoard.getHit()).isNotEqualTo(100);
//        }
//
//        @Test
//        void 성공_동시성_테스트_비관적_락_사용() throws InterruptedException {
//            int threadCount = 100;
//
//            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();
//
//            ExecutorService executorService = Executors.newFixedThreadPool(20);
//            CountDownLatch latch = new CountDownLatch(threadCount);
//
//            AtomicInteger hitCount = new AtomicInteger(0);
//            AtomicInteger missCount = new AtomicInteger(0);
//
//            for (int i = 0; i < threadCount; i++) {
//                executorService.execute(() -> {
//                    try {
//                        commentService.createCommentWithPessimisticLock(memberDto, board.getId(), commentRequestDto);
//                        hitCount.incrementAndGet();
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                        missCount.incrementAndGet();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//            latch.await();
//
//            System.out.println("hitCount = " + hitCount);
//            System.out.println("missCount = " + missCount);
//
//            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();
//
//            assertThat(newBoard.getHit()).isEqualTo(100);
//        }
//
//        @Test
//        void 성공_동시성_테스트_낙관적_락_사용() throws InterruptedException {
//            int threadCount = 100;
//
//            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();
//
//            ExecutorService executorService = Executors.newFixedThreadPool(20);
//            CountDownLatch latch = new CountDownLatch(threadCount);
//
//            AtomicInteger hitCount = new AtomicInteger(0);
//            AtomicInteger missCount = new AtomicInteger(0);
//
//            for (int i = 0; i < threadCount; i++) {
//                executorService.execute(() -> {
//                    try {
//                        commentService.createCommentWithOptimisticLock(memberDto, board.getId(), commentRequestDto);
//                        hitCount.incrementAndGet();
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                        missCount.incrementAndGet();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//            latch.await();
//
//            System.out.println("hitCount = " + hitCount);
//            System.out.println("missCount = " + missCount);
//
//            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();
//
//            assertThat(newBoard.getHit()).isEqualTo(100);
//        }
//
//        @Test
//        void 성공_동시성_테스트_분산_락_사용_redisson() throws InterruptedException {
//            int threadCount = 100;
//
//            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();
//
//            ExecutorService executorService = Executors.newFixedThreadPool(20);
//            CountDownLatch latch = new CountDownLatch(threadCount);
//
//            AtomicInteger hitCount = new AtomicInteger(0);
//            AtomicInteger missCount = new AtomicInteger(0);
//
//            for (int i = 0; i < threadCount; i++) {
//                executorService.execute(() -> {
//                    try {
//                        commentService.createCommentWithDistributedLock(memberDto, board.getId(), commentRequestDto);
//                        hitCount.incrementAndGet();
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                        missCount.incrementAndGet();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//            latch.await();
//
//            System.out.println("hitCount = " + hitCount);
//            System.out.println("missCount = " + missCount);
//
//            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();
//
//            assertThat(newBoard.getHit()).isEqualTo(100);
//        }
//    }
}