package com.ssafy.home.global.config.batch;

import com.ssafy.home.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledJobConfiguration {
    private final MemberService memberService;

    @Scheduled(cron ="0 * * * * *", zone = "Asia/Seoul")
    public void scheduledEndForm() {
        memberService.initAttempt();
    }
}