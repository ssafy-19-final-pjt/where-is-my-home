package com.ssafy.home.global.auth.filter;

import com.ssafy.home.domain.user.entity.Member;
import com.ssafy.home.domain.user.repository.MemberRepository;
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

        //토큰이 Valid한지 확인하기
        if(!jwtTokenProvider.validateToken(token)){
            filterChain.doFilter(request,response);
            return;
        }

        //userName 넣기, 문 열어주기
        Long userId = jwtTokenProvider.getInfoId("token");
        log.info("userId : {}", userId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Error: No member found with id " + userId));

        //Authenticationtoken 만들기
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getId(), null, List.of(new SimpleGrantedAuthority("DEFAULT_ROLE")));
        //디테일 설정하기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
