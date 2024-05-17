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

        try{
            if(memberRepository.existsByEmail(registerRequest.getEmail())){
                throw new Exception("이메일 중복 error 처리 요");
            };

            String salt  = encryption.getSalt();
            String password = encryption.Hashing(registerRequest.getPassword().getBytes(), salt);

            Member member = memberRepository.save(registerRequest.toEntity());
            GeneralMember generalMember = generalMemberRepository.save(GeneralMember.builder().member(member).userEncPassword(password).build());
            memberSecurityRepository.save(MemberSecret.builder().generalMember(generalMember).salt(salt).build());
            loginAttemptRepository.save(LoginAttempt.builder().member(member).build());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow();
        try {
            if(member.getLoginAttempt().getCount() >= 5){
                throw new Exception("시도 횟수 5 내일 다시 로그인 추가 요");
            }

            String salt = member.getGeneralMember().getMemberSecret().getSalt();

            String enc_password = encryption.Hashing(loginRequest.getPassword().getBytes(), salt);

            if (!member.getGeneralMember().getUserEncPassword().equals(enc_password)) {
                throw new Exception("error 처리 요");
            }

            String accessToken = jwtTokenProvider.createAccessToken(member);
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

            member.getLoginAttempt().initCount();

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            member.getLoginAttempt().updateCount();
            e.printStackTrace();
        }

        return null;

    }

    public String reissue(String refreshToken) {

        if(jwtTokenProvider.validateToken(refreshToken)){
            Long id = jwtTokenProvider.getInfoId(refreshToken);

            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("No member found with id: " + id));

            return jwtTokenProvider.createAccessToken(member);
        }

        return null;
    }
}
