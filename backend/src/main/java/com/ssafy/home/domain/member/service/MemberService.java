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
import com.ssafy.home.global.error.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberSecurityRepository memberSecurityRepository;
    private final GeneralMemberRepository generalMemberRepository;
    private final LoginAttemptRepository loginAttemptRepository;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
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
            throw new BusinessException(ErrorCode.INVALID_AES_KEY);
        }

        Member member = memberRepository.save(registerRequest.toEntity());
        GeneralMember generalMember = generalMemberRepository.save(GeneralMember.builder().member(member).userEncPassword(password).build());
        memberSecurityRepository.save(MemberSecret.builder().generalMember(generalMember).salt(salt).build());
        loginAttemptRepository.save(LoginAttempt.builder().member(member).build());

    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {

        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));

        if (member.getLoginAttempt().getCount() >= 5) {
            throw new AuthenticationException(ErrorCode.MEMBER_COUNT_OUT);
        }

        try {

            String salt = member.getGeneralMember().getMemberSecret().getSalt();

            String encPassword = encryption.Hashing(loginRequest.getPassword().getBytes(), salt);

            if (!member.getGeneralMember().getUserEncPassword().equals(encPassword)) {
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

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
    }

    @Transactional
    public void removeRefreshToken(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
        member.updateRefreshToken("");
    }

    @Transactional
    public void updatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));

        String salt = member.getGeneralMember().getMemberSecret().getSalt();

        String encPassword = null;
        try {
            encPassword = encryption.Hashing(password.getBytes(), salt);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_AES_KEY);
        }

        member.getGeneralMember().updatePassword(encPassword);
    }

    private final String MAIL_TITLE = "where-is-my-home의 비밀번호 변경 이메일 입니다.";

    @Transactional
    @Async("mailExecutor")
    public void sendPassword(String email) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));

        String newPassword = makeRandomPassword();

        try {
            String encPassword = encryption.Hashing(newPassword.getBytes(),member.getGeneralMember().getMemberSecret().getSalt());
            member.getGeneralMember().updatePassword(encPassword);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_AES_KEY);
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(MAIL_TITLE); // 메일 제목
            mimeMessageHelper.setText(setContext(newPassword, "password"), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.EMAIL_FAIL);
        }
    }


    static char[] charSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    private String makeRandomPassword(){

        StringBuilder pw = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            pw.append(charSet[idx]);
        }

        return String.valueOf(pw);
    }

    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}
