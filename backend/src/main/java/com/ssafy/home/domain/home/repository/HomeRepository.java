package com.ssafy.home.domain.home.repository;

import com.ssafy.home.domain.home.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    List<Home> findTop1000ByOrderByAptCodeAsc();
}
