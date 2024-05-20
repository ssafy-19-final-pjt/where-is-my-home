package com.ssafy.home.global.auth.util;

import com.ssafy.home.global.auth.constant.EmailConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class SendEmailLogic {

    private final SpringTemplateEngine templateEngine;

    public String makeRandomPassword(){

        StringBuilder pw = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (EmailConstraints.charSet.length * Math.random());
            pw.append(EmailConstraints.charSet[idx]);
        }

        return String.valueOf(pw);
    }

    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}
