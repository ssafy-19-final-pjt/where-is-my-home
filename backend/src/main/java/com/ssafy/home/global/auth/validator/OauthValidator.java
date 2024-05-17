package com.ssafy.home.global.auth.validator;

import com.ssafy.home.entity.member.MemberType;
import com.ssafy.home.global.error.ErrorCode;
import com.ssafy.home.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;


@Service
public class OauthValidator {

    public void validateMemberType(String memberType) {
        if(!MemberType.isMemberType(memberType)) {
            throw new BusinessException(ErrorCode.INVALID_MEMBER_TYPE);
        }
    }

}
