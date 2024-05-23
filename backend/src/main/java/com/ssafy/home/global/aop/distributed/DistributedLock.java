//package com.ssafy.home.global.aop.distributed;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.concurrent.TimeUnit;
//
//@Target(ElementType.METHOD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface DistributedLock {
//    //락의 이름
//    String key();
//
//    //락의 시간 단위
//    TimeUnit timeUnit() default TimeUnit.SECONDS;
//
//    //락을 기다리는 시간(default - 5s)
//    //락 획들을 위해 기다림
//    long waitTime() default 5L;
//
//    //락 임대시간(default - 3s)
//    //락 획득후 lease time이 지나면 락을 해제한다.
//    long leaseTime() default 3L;
//}
