package com.ssafy.home.global.auth.filter;

import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import com.ssafy.home.global.auth.dto.MemberDto;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String token = "";
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            refreshTokenCheck(request, response, token);
            throw new ExpiredJwtException(null, null, "header에 없거나, 형식이 틀립니다.");
        }

        try{
            token = authHeader.split(" ")[1].trim();
        } catch (Exception e) {
            refreshTokenCheck(request, response, token);
            throw new ExpiredJwtException(null, null, "액세스 토큰을 분리하는데 실패");
        }

        if(!jwtTokenProvider.validateToken(token)){
            refreshTokenCheck(request, response, token);
            throw new ExpiredJwtException(null, null, "액세스 토큰 만료");
        }

        Long userId = jwtTokenProvider.getInfoId(token);

        Member member = memberService.getMemberById(userId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .profile(member.getProfile())
                .build(),
                null, List.of(new SimpleGrantedAuthority("DEFAULT_ROLE")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void refreshTokenCheck(HttpServletRequest request, HttpServletResponse response, String token){
        Cookie[] cookies = request.getCookies();

        String refreshToken = null;

        if (cookies != null) {
            refreshToken =  Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        try {
            token = memberService.reissue(refreshToken);
        } catch (Exception e){
            throw new JwtException("리프레시 토큰 검증 실패");
        }
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, token);
    }

}
