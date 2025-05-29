package com.nhnacademy.traceloggermodule.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *  HTTP가 아닌 호출(예: MQTT, 스케줄러 등)에서도
 * 모든 서비스 메서드 진입 시점에
 * MDC에 traceId, source(서비스명)를 자동 주입하는 AOP Aspect.
 * 이후에 Flow/ResponseTime Aspect가 동작합니다.
 */
@Aspect
@Component
public class TraceIdAspect {

    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 서비스/컨트롤러 패키지 내 모든 public 메서드 실행 전
     * traceId, source를 MDC에 주입합니다.
     */
    @Before("execution(* com.nhnacademy..*Service.*(..)) "
            + "|| execution(* com.nhnacademy..*Controller.*(..))")
    public void initTraceId(JoinPoint jp) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("source", serviceName);
    }

    /**
     * 위 initTraceId로 세팅된 MDC를 메서드 실행 후 지웁니다.
     */
    @After("execution(* com.nhnacademy..*Service.*(..)) "
            + "|| execution(* com.nhnacademy..*Controller.*(..))")
    public void clearMdc(JoinPoint jp) {
        MDC.clear();
    }
}
