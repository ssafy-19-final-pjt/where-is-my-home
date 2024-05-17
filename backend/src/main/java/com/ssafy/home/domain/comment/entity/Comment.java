package com.ssafy.home.domain.comment.entity;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.domain.user.entity.Member;
import com.ssafy.home.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private long id;

    @Column(name = "content", nullable = false)
    private String content;

    @JoinColumn(name = "board_id", nullable = false)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @Builder
    private Comment(String content, Long boardId, Member member) {
        this.content = content;
        this.boardId = boardId;
        this.member = member;
    }
}
