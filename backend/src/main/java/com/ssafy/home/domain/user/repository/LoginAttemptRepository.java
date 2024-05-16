package com.ssafy.home.domain.user.repository;

import com.ssafy.home.domain.user.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
}
