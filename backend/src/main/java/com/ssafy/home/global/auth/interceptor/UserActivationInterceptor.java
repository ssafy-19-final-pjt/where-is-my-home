package com.ssafy.home.global.auth.interceptor;

import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.entity.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class UserActivationInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return false;
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberService.getMemberById(memberId);

        return true;
    }
}