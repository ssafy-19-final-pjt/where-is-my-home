package com.ssafy.home.global.config.security;

import com.ssafy.home.domain.member.repository.MemberRepository;
import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.global.auth.filter.JwtAuthenticationFilter;
import com.ssafy.home.global.auth.interceptor.UserActivationInterceptor;
import com.ssafy.home.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration implements WebMvcConfigurer {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    private final String[] REQUIRE_AUTH_PATH = {"/health/**", "member/logout", "member/info", "member/pw"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers((auth) -> auth.requestMatchers(REQUIRE_AUTH_PATH));
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, memberService), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().requestMatchers("/auth/social/**",
         "swagger-ui/index.html", "/v1/users/{id}",
         "/swagger-ui/**",
         "/swagger-resources/**",
         "/v3/api-docs/**",
         "/api-docs/**",
         "/api-docs", "/member/login");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserActivationInterceptor(memberService))
                .addPathPatterns(REQUIRE_AUTH_PATH);
    }
}
