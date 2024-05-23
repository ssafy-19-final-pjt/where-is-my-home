package com.ssafy.home.domain.comment.service;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.comment.dto.response.CommentResponseDto;
import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.entity.member.Member;
import groovy.util.logging.Slf4j;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@ExtendWith(MockitoExtension.class)
class CommentResponseMapperTest extends TestConfig {
    private static final Logger log = LoggerFactory.getLogger(CommentResponseMapperTest.class);
    @InjectMocks
    private CommentResponseMapper commentResponseMapper;

    @Mock
    private Member member;

    @Mock
    private Board board;

    private List<Comment> comments;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        comments = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            comments.add(Comment.builder().content("Content " + i).member(member).board(board).build());
        }
    }

    @Test
    void 성공_댓글_리스트를_받을_시_응답_형식에_맞게_매핑된다() {
        // given
        // when
        List<CommentResponseDto> result = commentResponseMapper.toListCommentResponse(comments);
        
        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isNotNull()
                    .hasSize(5)
                    .allMatch(dto -> dto.getContent().startsWith("Content"));
        });
    }
}