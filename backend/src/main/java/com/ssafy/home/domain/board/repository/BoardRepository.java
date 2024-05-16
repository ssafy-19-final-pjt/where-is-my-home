package com.ssafy.home.domain.board.repository;

import com.ssafy.home.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
