package com.ssafy.home.domain.comment.service;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.dto.MemberDto;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CommentServiceTest extends TestConfig {
    private final CommentService commentService;
    private final MemberService memberService;
    private final BoardRepository boardRepository;
    private Member member;
    private Board board;
    private MemberDto memberDto;

    @Autowired
    public CommentServiceTest(CommentService commentService, MemberService memberService,
                              BoardRepository boardRepository) {
        this.commentService = commentService;
        this.memberService = memberService;
        this.boardRepository = boardRepository;
    }


    @BeforeEach
    void before() {
        member = memberService.getMemberById(11L);
        board = boardRepository.findById(11L).orElseThrow(() -> new RuntimeException());
        memberDto = MemberDto.builder().id(member.getId()).name(member.getName()).build();
    }

    @Nested
    class Commnet {
        @Test
        @Transactional
        void 성공_조회수_추가() {
            //given
            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();
            Board newBoard = boardRepository.save(Board.builder()
                    .content("test")
                    .member(member)
                    .title("testTitle")
                    .hit(0)
                    .build());
            //when
            for (int i = 0; i < 100; i++) {
                commentService.createComment(memberDto, newBoard.getId(), commentRequestDto);
            }

            //then
            assertThat(newBoard.getHit()).isEqualTo(100);
        }

        @Test
        void 동시성_테스트() throws InterruptedException {
            int threadCount = 100;

            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();

            ExecutorService executorService = Executors.newFixedThreadPool(20);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger hitCount = new AtomicInteger(0);
            AtomicInteger missCount = new AtomicInteger(0);

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        commentService.createComment(memberDto, board.getId(), commentRequestDto);
                        hitCount.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        missCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            System.out.println("hitCount = " + hitCount);
            System.out.println("missCount = " + missCount);

            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();

            assertThat(newBoard.getHit()).isEqualTo(100);
        }

        @Test
        void 동시성_테스트_비관적_락_사용() throws InterruptedException {
            int threadCount = 100;

            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();

            ExecutorService executorService = Executors.newFixedThreadPool(20);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger hitCount = new AtomicInteger(0);
            AtomicInteger missCount = new AtomicInteger(0);

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        commentService.createCommentWithPessimisticLock(memberDto, board.getId(), commentRequestDto);
                        hitCount.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        missCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            System.out.println("hitCount = " + hitCount);
            System.out.println("missCount = " + missCount);

            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();

            assertThat(newBoard.getHit()).isEqualTo(100);
        }

        @Test
        void 동시성_테스트_낙관적_락_사용() throws InterruptedException {
            int threadCount = 100;

            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch latch = new CountDownLatch(threadCount);

            AtomicInteger hitCount = new AtomicInteger(0);
            AtomicInteger missCount = new AtomicInteger(0);

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(() -> {
                    try {
                        commentService.createCommentWithOptimisticLock(memberDto, board.getId(), commentRequestDto);
                        hitCount.incrementAndGet();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        missCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            System.out.println("hitCount = " + hitCount);
            System.out.println("missCount = " + missCount);

            Board newBoard = boardRepository.findById(board.getId()).orElseThrow();

            assertThat(newBoard.getHit()).isEqualTo(100);
        }
    }
}