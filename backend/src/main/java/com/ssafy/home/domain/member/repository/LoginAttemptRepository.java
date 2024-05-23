package com.ssafy.home.domain.member.repository;

import com.ssafy.home.entity.member.LoginAttempt;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    @Modifying
    @Query("update LoginAttempt la set la.count = la.count + 1 where la.id = :id")
    void updateCount(@Param("id")long id);
}
