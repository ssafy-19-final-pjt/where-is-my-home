package com.ssafy.home.domain.member.service;

import com.ssafy.home.domain.member.dto.request.LoginRequest;
import com.ssafy.home.domain.member.dto.request.RegisterRequest;
import com.ssafy.home.domain.member.dto.response.TokenResponse;
import com.ssafy.home.domain.member.repository.GeneralMemberRepository;
import com.ssafy.home.domain.member.repository.LoginAttemptRepository;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.domain.member.repository.MemberSecurityRepository;
import com.ssafy.home.entity.member.GeneralMember;
import com.ssafy.home.entity.member.LoginAttempt;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.entity.member.MemberSecret;
import com.ssafy.home.global.auth.Encryption;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberSecurityRepository memberSecurityRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final LoginAttemptRepository loginAttemptRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final Encryption encryption;

    @Transactional
    public void register(RegisterRequest registerRequest) {

        if (memberRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH);
        }

        String salt = encryption.getSalt();
        String password = "";

        try {
            password = encryption.Hashing(registerRequest.getPassword().getBytes(), salt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Member member = memberRepository.save(registerRequest.toEntity());
        GeneralMember generalMember = generalMemberRepository.save(GeneralMember.builder().member(member).userEncPassword(password).build());
        memberSecurityRepository.save(MemberSecret.builder().generalMember(generalMember).salt(salt).build());
        loginAttemptRepository.save(LoginAttempt.builder().member(member).build());

    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.ALREADY_REGISTERED_MEMBER));

        if (member.getLoginAttempt().getCount() >= 5) {
            throw new AuthenticationException(ErrorCode.MEMBER_COUNT_OUT);
        }

        try {

            String salt = member.getGeneralMember().getMemberSecret().getSalt();

            String enc_password = encryption.Hashing(loginRequest.getPassword().getBytes(), salt);

            if (!member.getGeneralMember().getUserEncPassword().equals(enc_password)) {
                throw new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH);
            }

            String accessToken = jwtTokenProvider.createAccessToken(member);
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

            member.getLoginAttempt().initCount();

            member.updateRefreshToken(refreshToken);

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e){
            member.getLoginAttempt().updateCount();
            throw new AuthenticationException(e.getErrorCode());
        } catch (Exception e) {
            member.getLoginAttempt().updateCount();
            e.printStackTrace();
        }

        return null;

    }

    public String reissue(String refreshToken) {

        if (jwtTokenProvider.validateToken(refreshToken)) {
            Long id = jwtTokenProvider.getInfoId(refreshToken);

            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

            return jwtTokenProvider.createAccessToken(member);
        }

        return null;
    }
}
