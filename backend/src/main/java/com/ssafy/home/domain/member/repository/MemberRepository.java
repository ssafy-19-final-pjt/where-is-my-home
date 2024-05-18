package com.ssafy.home.domain.member.repository;

import com.ssafy.home.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    @Query("Select m from member m join fetch m.generalMember join fetch m.generalMember.memberSecret where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

}
