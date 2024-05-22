package com.ssafy.home.domain.member.service;

import com.ssafy.home.config.TestConfig;
import com.ssafy.home.domain.member.dto.response.TokenResponse;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.Encryption;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.AuthenticationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[회원가입, ...]")
class MemberServiceTest extends TestConfig {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final Encryption encryption;

    @Autowired
    public MemberServiceTest(MemberService memberService, MemberRepository memberRepository, Encryption encryption) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.encryption = encryption;
    }

    @Nested
    class 회원_가입 {
        private final String name = "String11";
        private final String email = "String11";
        private final String password = "String11";

        @Test
        void 성공_회원가입시() {
            //given

            //when
            memberService.register(name, email, password);

            //then
            Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
            Assertions.assertThat(name).isEqualTo(findMember.getName());
        }

        @Test
        void 에러_회원가입시_이메일_중복이_일어나면_오류가_발생한다() {
            //given
            memberService.register(name, email, password);

            //when //then
            assertThatThrownBy(() -> memberService.register(name, email, password))
                    .isInstanceOf(AuthenticationException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ALREADY_REGISTERED_MEMBER);
        }
    }

    @Nested
    class 로그인 {
        private final String email = "test@test.com";

        @Test
        void 성공_로그인시_refresh_token_확인(){
            //given
            final String password = "string";

            //when
            TokenResponse tokenResponse = memberService.login(email,password);

            //then
            Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
            Assertions.assertThat(tokenResponse.getRefreshToken()).isEqualTo(findMember.getRefreshToken());
        }

        @Test
        void 에러_비밀번호가_틀렷을_시_오류가_발생한다() {
            //given
            final String password = "error";

            //when //then
            assertThatThrownBy(() -> memberService.login(email, password))
                    .isInstanceOf(AuthenticationException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_NOT_MATCH);
        }
    }

    @Nested
    class 아이디로_맴버_찾기 {

        @Test
        void 성공_아이디로_맴버_찾기() {
            //given
            final Long memberId = 11L;
            //when
            Member member = memberService.getMemberById(memberId);

            //then
            Assertions.assertThat(memberId).isEqualTo(member.getId());
        }

        @Test
        void 에러_해당_아이디가_없을시_오류가_발생한다() {
            //given
            final Long wrongMemberId = 12L;

            //when //then
            assertThatThrownBy(() -> memberService.getMemberById(wrongMemberId))
                    .isInstanceOf(AuthenticationException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_NOT_EXISTS);
        }
    }

    @Nested
    class 리프레시_토큰_삭제{
        @Test
        void 성공_리프레시_토큰_삭제시() {
            //given
            String email = "test@test.com";
            final Long memberId = 11L;

            //when
            memberService.removeRefreshToken(memberId);

            //then
            Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
            Assertions.assertThat("").isEqualTo(findMember.getRefreshToken());
        }
    }



    @Nested
    class 성공_패스워드_변경시{
        @Test
        void 성공_패스워드_변경시() throws Exception {
            //given
            final Long memberId = 11L;
            final String email = "test@test.com";
            String newPassword = "String12";

            //when
            memberService.updatePassword(memberId, newPassword);

            //then
            Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
            newPassword = encryption.Hashing(newPassword.getBytes(), findMember.getGeneralMember().getMemberSecret().getSalt());
            Assertions.assertThat(newPassword).isEqualTo(findMember.getGeneralMember().getUserEncPassword());
        }
    }
}
