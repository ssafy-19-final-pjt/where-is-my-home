package com.ssafy.home.domain.member.repository;

import com.ssafy.home.entity.member.MemberSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSecurityRepository extends JpaRepository<MemberSecret, Long> {
}
