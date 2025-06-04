package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

/**
 * Controller/Service 진입 시
 * 자동으로 trace-flow 로그를 남기는 AOP Aspect.
 */
@Aspect
@Slf4j
@Order(1)
public class FlowLoggingAspect {

    @Before("execution(* com.nhnacademy..*Controller.*(..)) "
            + "|| execution(* com.nhnacademy..*Service.*(..))")
    public void logFlow(JoinPoint jp) {
        FlowLogger.log(jp.getSignature().toShortString(), null);
    }
}
