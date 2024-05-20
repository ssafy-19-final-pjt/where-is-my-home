package com.ssafy.home.global.auth.validator;

import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenValidator {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean validate(String refreshToken) {
        Long memberId = jwtTokenProvider.getInfoId(refreshToken);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));

        return member.getRefreshToken().equals(refreshToken);
    }
}
