package com.ssafy.home.domain.user.repository;

import com.ssafy.home.domain.user.entity.MemberSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSecurityRepository extends JpaRepository<MemberSecret, Long> {
}
