package com.ssafy.home.global.config.security;

import com.ssafy.home.domain.member.service.MemberService;
import com.ssafy.home.global.auth.filter.JwtAuthenticationFilter;
import com.ssafy.home.global.auth.filter.JwtExceptionFilter;
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
@EnableWebSecurity
public class WebSecurityConfiguration implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers((auth) -> auth.requestMatchers(WebSecurityPath.REQUIRE_AUTH_PATH.getPaths()));
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, memberService), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

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
                .addPathPatterns(WebSecurityPath.REQUIRE_AUTH_PATH.getPaths());
    }
}
