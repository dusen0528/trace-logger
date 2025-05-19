package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class ResponseTimeAspect {
    @Around("execution(* com.nhnacademy..*Controller.*(..)) || execution(* com.nhnacademy..*Service.*(..))")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long rt = System.currentTimeMillis() - start;
            Map<String,Object> m = Map.of(
                    "traceId",       MDC.get("traceId"),
                    "source",        MDC.get("source"),
                    "target",        pjp.getSignature().getDeclaringTypeName()+"#"+pjp.getSignature().getName(),
                    "response_time", rt
            );
            log.info("{}", m);
        }
    }
}