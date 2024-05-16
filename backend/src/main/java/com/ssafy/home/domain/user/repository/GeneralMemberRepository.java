package com.ssafy.home.domain.user.repository;

import com.ssafy.home.domain.user.entity.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralMemberRepository extends JpaRepository<GeneralMember, Long> {
}
