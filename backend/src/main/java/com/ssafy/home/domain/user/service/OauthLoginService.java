package com.ssafy.home.domain.user.service;


import com.ssafy.home.domain.user.dto.OauthLoginDto;
import com.ssafy.home.domain.user.entity.Member;
import com.ssafy.home.domain.user.entity.MemberType;
import com.ssafy.home.domain.user.repository.MemberRepository;
import com.ssafy.home.external.oauth.model.OAuthAttributes;
import com.ssafy.home.external.oauth.service.SocialLoginApiService;
import com.ssafy.home.external.oauth.service.SocialLoginApiServiceFactory;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import com.ssafy.home.global.auth.jwt.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public OauthLoginDto.Response oauthLogin(String accessToken, MemberType memberType) {
        SocialLoginApiService socialLoginApiService = SocialLoginApiServiceFactory.getSocialLoginApiService(memberType);
        OAuthAttributes userInfo = socialLoginApiService.getUserInfo(accessToken);
        log.info("userInfo : {}",  userInfo);

        JwtTokenDto jwtTokenDto;
        Optional<Member> optionalMember = memberRepository.findByEmail(userInfo.getEmail());
        if(optionalMember.isEmpty()) { // 신규 회원 가입
            Member oauthMember = userInfo.toMemberEntity(memberType);
            oauthMember = memberRepository.save(oauthMember);

            // 토큰 생성
            jwtTokenDto = jwtTokenProvider.createJwtTokenResponse(oauthMember);
            oauthMember.updateRefreshToken(jwtTokenDto.getRefreshToken());
        } else { // 기존 회원일 경우
            Member oauthMember = optionalMember.get();

            // 토큰 생성
            jwtTokenDto = jwtTokenProvider.createJwtTokenResponse(oauthMember);
            oauthMember.updateRefreshToken(jwtTokenDto.getRefreshToken());
        }

        return OauthLoginDto.Response.of(jwtTokenDto);
    }

}

