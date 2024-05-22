package com.ssafy.home.domain.board.repository;

import com.ssafy.home.domain.board.entity.Board;
import feign.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("""
        SELECT b 
        FROM Board b 
        WHERE b.id < :cursor 
        ORDER BY b.id 
        DESC
        LIMIT :limit
    """)
    List<Board> findNextPage(@Param("cursor") Long cursor, @Param("limit") int limit);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByIdPessimisticLock(@Param("id") Long id);


    @Lock(LockModeType.OPTIMISTIC)
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findByIdOptimisticLock(@Param("id") Long id);
}
