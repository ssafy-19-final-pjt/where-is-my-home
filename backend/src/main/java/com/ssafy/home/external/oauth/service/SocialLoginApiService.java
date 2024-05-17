package com.ssafy.home.external.oauth.service;


import com.ssafy.home.external.oauth.model.OAuthAttributes;

public interface SocialLoginApiService {

    OAuthAttributes getUserInfo(String accessToken);

}
