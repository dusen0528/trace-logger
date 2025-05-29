package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;

/**
 *  Controller/Service 메서드 실행 시간을 측정하고,
 * JSON 로그로 'response_time' 필드를 남기는 AOP Aspect.
 */
@Aspect
@Slf4j
public class ResponseTimeAspect {

    @Around("execution(* com.nhnacademy..*Controller.*(..)) "
            + "|| execution(* com.nhnacademy..*Service.*(..))")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;

        Map<String, Object> data = Map.of(
                "target", pjp.getSignature().toShortString(),
                "response_time", elapsed
        );
        log.info("response-time", StructuredArguments.entries(data));

        return result;
    }
}
