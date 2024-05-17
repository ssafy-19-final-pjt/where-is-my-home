package com.ssafy.home.domain.board.entity;

import com.ssafy.home.domain.comment.entity.Comment;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "hit", nullable = false)
    private int hit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "board")
    List<Comment> commentList = new ArrayList<>();

    @Builder
    private Board(String title, String content, int hit, Member member) {
        this.title = title;
        this.content = content;
        this.hit = hit;
        this.member = member;
    }
}