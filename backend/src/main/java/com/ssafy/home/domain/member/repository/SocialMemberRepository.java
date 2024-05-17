package com.ssafy.home.domain.member.repository;

import com.ssafy.home.entity.member.Member;
import com.ssafy.home.entity.member.MemberType;
import com.ssafy.home.entity.member.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {
    Optional<SocialMember> findByMemberAndMemberType(Member oauthMember, MemberType memberType);
}
