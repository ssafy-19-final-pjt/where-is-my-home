package com.ssafy.home.domain.comment.repository;

import com.ssafy.home.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
