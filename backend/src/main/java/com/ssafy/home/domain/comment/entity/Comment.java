package com.ssafy.home.domain.comment.entity;

import com.ssafy.home.domain.board.entity.Board;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private Comment(String content, Board board, Member member) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Builder
    private Comment(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        this.board = board;
    }
}


