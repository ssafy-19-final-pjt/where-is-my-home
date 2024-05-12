package com.ssafy.home.domain.user.repository;

import com.ssafy.home.domain.user.domain.MemberLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptCount extends JpaRepository<MemberLoginAttempt, Long> {
}
