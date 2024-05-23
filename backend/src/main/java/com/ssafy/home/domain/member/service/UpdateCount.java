package com.ssafy.home.domain.member.service;

import com.ssafy.home.domain.member.repository.LoginAttemptRepository;
import com.ssafy.home.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UpdateCount {

    private final LoginAttemptRepository loginAttemptRepository;

    public void updateCount(Member member) {
        loginAttemptRepository.updateCount(member.getLoginAttempt().getId());
    }
}
