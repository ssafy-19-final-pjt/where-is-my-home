package com.ssafy.home.domain.user.service;

import com.ssafy.home.domain.user.dto.request.LoginRequest;
import com.ssafy.home.domain.user.dto.request.RegisterRequest;
import com.ssafy.home.domain.user.dto.response.TokenResponse;

public interface MemberService {
    void register(RegisterRequest registerRequest);

    TokenResponse login(LoginRequest loginRequest);

    String reissue(String refreshToken);
}
