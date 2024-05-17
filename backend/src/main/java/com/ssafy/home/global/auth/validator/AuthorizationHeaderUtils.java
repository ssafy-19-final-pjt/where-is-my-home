package com.ssafy.home.global.auth.validator;

import com.ssafy.home.global.auth.constant.GrantType;
import org.springframework.util.StringUtils;

public class AuthorizationHeaderUtils {

    public static void validateAuthorization(String authorizationHeader) {

        try {
            // 1. authorizationHeader 필수 체크
            if (!StringUtils.hasText(authorizationHeader)) {
                throw new Exception("error 처리 요");
            }

            // 2. authorizationHeader Bearer 체크
            String[] authorizations = authorizationHeader.split(" ");
            if (authorizations.length < 2 || (!GrantType.BEARER.getType().equals(authorizations[0]))) {
                throw new Exception("error 처리 요");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
