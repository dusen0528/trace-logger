package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;

@Aspect
@Component
@Slf4j
public class ResponseTimeAspect {

    // Controller·Service 진입점 모두 감싸기
    @Around("execution(* com.nhnacademy..*Controller.*(..)) || execution(* com.nhnacademy..*Service.*(..))")
    public Object measureResponseTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = pjp.proceed();
            return result;
        } finally {
            long end = System.currentTimeMillis();
            long rt = end - start;
            String traceId = MDC.get("traceId");
            String source  = MDC.get("source");
            String target  = pjp.getSignature().getDeclaringTypeName() + "#" + pjp.getSignature().getName();

            log.info(
                    "{{\"traceId\":\"{}\",\"source\":\"{}\",\"target\":\"{}\",\"response_time\":{}}}",
                    traceId, source, target, rt
            );
        }
    }
}
