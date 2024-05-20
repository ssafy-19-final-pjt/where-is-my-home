package com.ssafy.home.domain.comment.repository;

import com.ssafy.home.domain.comment.entity.Comment;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
        select c
        from Comment c
        where c.board.id = :boardId
        """)
    List<Comment> findAllByBoardId(@Param("boardId")Long boardId);

    @Modifying
    @Query("""
        delete from Comment c where c.board.id =:boardId
    """)
    void deleteAllByBoardId(@Param("boardId") Long boardId);
}
