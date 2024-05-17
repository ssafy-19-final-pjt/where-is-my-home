package com.ssafy.home.external.oauth.kakaoToken.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class KakaoTokenDto {

    @NoArgsConstructor
    @Getter
    public static class Request {
        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;

        @Builder
        public Request(String grant_type, String client_id, String redirect_uri, String code, String client_secret) {
            this.grant_type = grant_type;
            this.client_id = client_id;
            this.redirect_uri = redirect_uri;
            this.code = code;
            this.client_secret = client_secret;
        }
    }

    @ToString
    @Getter
    @NoArgsConstructor
    public static class Response {
        private String token_type;
        private String access_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;

        @Builder
        public Response(String token_type, String access_token, Integer expires_in, String refresh_token, Integer refresh_token_expires_in, String scope) {
            this.token_type = token_type;
            this.access_token = access_token;
            this.expires_in = expires_in;
            this.refresh_token = refresh_token;
            this.refresh_token_expires_in = refresh_token_expires_in;
            this.scope = scope;
        }
    }

}
