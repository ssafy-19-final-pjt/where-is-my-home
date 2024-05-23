//package com.ssafy.home.global.aop.distributed;
//
//import com.ssafy.home.global.util.CustomSpringELParser;
//import groovy.util.logging.Slf4j;
//import java.lang.reflect.Method;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//
//@lombok.extern.slf4j.Slf4j
//@Slf4j
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Order(Ordered.LOWEST_PRECEDENCE - 1)
//public class DistributedLockAop {
//    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
//    private final RedissonClient redissonClient;
//    private final AopForTransaction aopForTransaction;
//
//    @Around("@annotation(com.ssafy.home.global.aop.distributed.DistributedLock)")
//    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
//
//        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(),
//                joinPoint.getArgs(), distributedLock.key());
//        RLock rLock = redissonClient.getLock(key);
//
//        try {
//            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
//                    distributedLock.timeUnit());
//            if (!available) {
//                return false;
//            }
//            log.info("락 획득");
//            return aopForTransaction.proceed(joinPoint);
//        } catch (InterruptedException e) {
//            throw new InterruptedException();
//        } finally {
//            try {
//                rLock.unlock();
//                log.info("락 해제");
//            } catch (IllegalMonitorStateException e) {
//            }
//        }
//    }
//}
