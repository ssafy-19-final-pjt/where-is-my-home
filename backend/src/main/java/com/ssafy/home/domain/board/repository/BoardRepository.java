package com.ssafy.home.domain.board.repository;

import com.ssafy.home.domain.board.entity.Board;
import feign.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("""
        SELECT b
        FROM Board b
        join fetch b.member
        WHERE b.id = :id
    """)
    Optional<Board> findById(@Param("id")Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByIdPessimisticLock(@Param("id") Long id);
}
