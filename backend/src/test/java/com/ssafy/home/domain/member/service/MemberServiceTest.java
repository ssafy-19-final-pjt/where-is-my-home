package com.ssafy.home.domain.member.service;

import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.Encryption;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.AuthenticationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Encryption encryption;

    @Test
    void 성공_회원가입() {
        //given
        String name = "String11";
        String email = "String11";
        String password = "String11";

        //when
        memberService.register(name, email, password);

        //then
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));
        Assertions.assertThat(name).isEqualTo(findMember.getName());
    }

    @Test
    void 성공_아이디로맴버찾기() {
        //given

        //when
        Member member = memberService.getMemberById(11L);

        //then
        Assertions.assertThat(11L).isEqualTo(member.getId());
    }

    @Test
    void 성공_리프레시토큰삭제() {
        //given
        String email = "test@test.com";

        //when
        memberService.removeRefreshToken(11L);

        //then
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));
        Assertions.assertThat("").isEqualTo(findMember.getRefreshToken());
    }

    @Test
    void 성공_패스워드변경() throws Exception {
        //given
        String email = "test@test.com";
        String newPassword = "String12";

        //when
        memberService.updatePassword(11L, newPassword);

        //then
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));
        newPassword = encryption.Hashing(newPassword.getBytes(),findMember.getGeneralMember().getMemberSecret().getSalt());
        Assertions.assertThat(newPassword).isEqualTo(findMember.getGeneralMember().getUserEncPassword());
    }
}