package com.ssafy.home.global.error.exception;

import com.ssafy.home.global.error.ErrorCode;

public class BadRequestException extends BusinessException{
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, String message){
        super(errorCode, message);
    }
}
