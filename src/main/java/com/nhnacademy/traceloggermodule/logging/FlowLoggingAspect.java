package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


/**
 *   AOP 기반으로 컨트롤러/서비스 진입 시 흐름 로그를 자동 출력
 * - 비침투적이고 일관된 로깅 구조 확보 가능
 * - 확장성: Pointcut 조정으로 원하는 범위에만 적용 가능
 */
@Aspect
@Component
@Slf4j
public class FlowLoggingAspect {
    @Before("execution(* com.nhnacademy..*Controller.*(..)) || execution(* com.nhnacademy..*Service.*(..))")
    public void logFlow(JoinPoint jp) {
        FlowLogger.log(
                jp.getSignature().toShortString(),
                null  // payload 없으면 null
        );
    }
}