package com.ssafy.home.global.auth.filter;

import com.ssafy.home.entity.member.Member;
import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.global.auth.dto.MemberDto;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // Jwt Provier 주입
    public JwtAuthenticationFilter(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("header에 없거나, 형식이 틀립니다. - {}", authHeader);
            filterChain.doFilter(request,response);
            return;
        }

        String token;
        try{
            token = authHeader.split(" ")[1].trim();
        } catch (Exception e) {
            log.error("토큰을 분리하는데 실패했습니다. - {}", authHeader);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("token : {}", token);

        if(!jwtTokenProvider.validateToken(token)){
            filterChain.doFilter(request,response);
            return;
        }

        Long userId = jwtTokenProvider.getInfoId(token);
        log.info("userId : {}", userId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Error: No member found with id " + userId));

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

}
