package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;

/**
 * 메서드 실행 시간을 측정해
 * MDC에 'response_time' 값을 추가한다.
 */
@Aspect
@Slf4j
public class ResponseTimeAspect {

    /**
     * Controller, Service 메서드 실행 전후로 소요시간 계산.
     * @param pjp 진행 중인 조인포인트
     * @return 실제 메서드 반환값
     * @throws Throwable 내부 예외 전파
     */
    @Around("execution(* com.nhnacademy..*Controller.*(..)) "
            + "|| execution(* com.nhnacademy..*Service.*(..))")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsed = System.currentTimeMillis() - start;
        MDC.put("response_time", String.valueOf(elapsed));
        return result;
    }
}
