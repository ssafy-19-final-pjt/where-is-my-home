package com.ssafy.home.domain.member.repository;

import com.ssafy.home.entity.member.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {
}
