package com.ssafy.home.domain.member.repository;

import com.ssafy.home.entity.member.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
}
