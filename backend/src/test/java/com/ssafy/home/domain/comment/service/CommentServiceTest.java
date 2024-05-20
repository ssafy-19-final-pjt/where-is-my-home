package com.ssafy.home.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.board.repository.BoardRepository;
import com.ssafy.home.domain.board.service.BoardReadService;
import com.ssafy.home.domain.comment.dto.request.CommentRequestDto;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.dto.MemberDto;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class CommentServiceTest extends TestConfig {

    private static final Logger log = LoggerFactory.getLogger(CommentServiceTest.class);
    private final CommentService commentService;
    private final MemberService memberService;
    private final BoardRepository boardRepository;
    private final BoardReadService boardReadService;
    private final EntityManager em;

    private Member member;
    private Board board;
    private MemberDto memberDto;

    @Autowired
    public CommentServiceTest(CommentService commentService, MemberService memberService,
                              BoardRepository boardRepository, BoardReadService boardReadService, EntityManager em) {
        this.commentService = commentService;
        this.memberService = memberService;
        this.boardRepository = boardRepository;
        this.boardReadService = boardReadService;
        this.em = em;
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
        void 성공_조회수_추가() {
            //given
            CommentRequestDto commentRequestDto = CommentRequestDto.builder().content("test").build();

            //when
            for (int i = 0; i < 100; i++) {
                commentService.createComment(memberDto, board.getId(), commentRequestDto);
            }

            //then
            assertThat(board.getHit()).isEqualTo(100);
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

            Assertions.assertThat(newBoard.getHit()).isEqualTo(100);
        }
    }
}