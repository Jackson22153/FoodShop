package com.phucx.shop.aspects;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(1)
@Component
public class LoggerAspect {
    
    @Around("execution(* com.phucx.shop.service.*.*.*(..))")
    public Object loggerAspectService(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info(joinPoint.getSignature().toString() + " has been invoked");
        Instant begin = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        Long durationTime = Duration.between(begin, end).toMillis();
        log.info(joinPoint.getSignature().toString() + " is ended");
        log.info(joinPoint.getSignature() + " takes " + durationTime + " ms to execute");
        return result;
    }
}
