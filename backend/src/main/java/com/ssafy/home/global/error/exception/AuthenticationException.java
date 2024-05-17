package com.ssafy.home.global.error.exception;


import com.ssafy.home.global.error.ErrorCode;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

}
