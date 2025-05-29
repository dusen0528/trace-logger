package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 서비스/컨트롤러 메서드 진입 시
 * MDC 기반으로 trace-flow 로그를 자동 출력한다.
 */
@Aspect
@Slf4j
public class FlowLoggingAspect {

    /**
     * Controller, Service 패키지의 모든 public 메서드 실행 전 호출된다.
     * @param jp 조인포인트 정보
     */
    @Before("execution(* com.nhnacademy..*Controller.*(..)) "
            + "|| execution(* com.nhnacademy..*Service.*(..))")
    public void logFlow(JoinPoint jp) {
        FlowLogger.log(jp.getSignature().toShortString(), null);
    }
}
