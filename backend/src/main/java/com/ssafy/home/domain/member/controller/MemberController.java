package com.ssafy.home.domain.member.controller;

import com.ssafy.home.domain.member.dto.request.FindPasswordRequest;
import com.ssafy.home.domain.member.dto.request.LoginRequest;
import com.ssafy.home.domain.member.dto.request.UpdatePasswordRequest;
import com.ssafy.home.domain.member.dto.request.RegisterRequest;
import com.ssafy.home.domain.member.dto.response.TokenResponse;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.global.auth.dto.MemberDto;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import com.ssafy.home.global.auth.validator.RefreshTokenValidator;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.AuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RefreshTokenValidator refreshTokenValidator;
    private final MemberRepository memberRepository;

    @Tag(name = "authentication")
    @Operation(summary = "일반 회원가입 API", description = "일반 회원가입 API")
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){

        memberService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.OK).body("signup success");
    }

    @Tag(name = "authentication")
    @Operation(summary = "일반 로그인 API", description = "일반 로그인 API")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){

        TokenResponse tokenResponse = memberService.login(loginRequest);

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, tokenResponse.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body("token success");
    }

    @Tag(name = "authentication")
    @Operation(summary = "액세스 토큰 재발급 API", description = "액세스 토큰 재발급 API")
    @GetMapping("/accessToken")
    public ResponseEntity<String> createAccessToken(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        String refreshToken = null;

        if (cookies != null) {
            refreshToken =  Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if(!refreshTokenValidator.validate(refreshToken)){
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, memberService.reissue(refreshToken));

        return ResponseEntity.status(HttpStatus.OK).body("access token");
    }

    @Tag(name = "authentication")
    @Operation(summary = "로그아웃 API", description = "로그아웃 API")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal MemberDto memberDto,
                                         HttpServletResponse response) {

        Cookie myCookie = new Cookie("refreshToken", null);
        myCookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        myCookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(myCookie);

        memberService.removeRefreshToken(memberDto.getId());

        return ResponseEntity.status(HttpStatus.OK).body("logout success");
    }

    @Tag(name = "authentication")
    @Operation(summary = "비밀번호 변경 API", description = "비밀번호 변경 API")
    @PostMapping("/pw")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal MemberDto memberDto,
                                                 @RequestBody UpdatePasswordRequest updatePasswordRequest){

        memberService.updatePassword(memberDto.getId(),updatePasswordRequest.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body("updatePassword success");
    }

    @Tag(name = "authentication")
    @Operation(summary = "비밀번호 찾기 API", description = "비밀번호 찾기 API")
    @PostMapping("/findPw")
    public ResponseEntity<String> findPassword(@RequestBody FindPasswordRequest findPasswordRequest){

        memberService.sendPassword(findPasswordRequest.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(findPasswordRequest.getEmail() + "send password success");
    }

    @Tag(name = "authentication")
    @Operation(summary = "유저 정보 가져오기 API", description = "유저 정보 가져오기 API")
    @PostMapping("/info")
    public ResponseEntity<MemberDto> findPassword(@AuthenticationPrincipal MemberDto memberDto){

        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
    }


}
