package com.ssafy.home.global.aop.retry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.LOWEST_PRECEDENCE-1)
@Aspect
@Component
public class RetryAspect {
    private static final Logger log = LoggerFactory.getLogger(RetryAspect.class);

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable{
        int maxRetry = retry.value();
        Exception exceptionHolder = null;
        for(int retryCount=1; retryCount<=maxRetry; retryCount++){
            try{
                return joinPoint.proceed();
            }
            catch (Exception e){
                log.warn("[retry] try count = {}/{}", retryCount, maxRetry);
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
