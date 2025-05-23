package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * {@link ResponseTimeAspect}는 모든 Controller 및 Service 계층의 메서드 호출에 대해
 * 실행 시간을 측정하고, 측정 결과를 로그로 남기는 AOP 어스펙트입니다.
 *
 * <p>
 * AOP Around advice를 통해 메서드 진입 시점에 현재 시각을 MDC에 저장하고,
 * 메서드 종료 시점에 걸린 시간을 계산하여 로그로 기록합니다.
 * Logstash 파이프라인에서 response_time 필드를 파싱하도록 구성되어 있어,
 * 분산 트레이싱 및 응답 시간 모니터링에 활용됩니다.
 * </p>
 */
@Aspect
@Component
@Slf4j
public class ResponseTimeAspect {

    /**
     * Controller와 Service 계층의 모든 public 메서드를 감싸서 실행 시간을 측정합니다.
     * <ul>
     *   <li>메서드 진입 시점에 {@code System.currentTimeMillis()}를 MDC의 "time" 키로 저장</li>
     *   <li>메서드 종료 시점에 경과 시간을 계산해 "response_time"으로 기록</li>
     *   <li>MDC의 traceId, source 정보 및 호출 대상(target) 정보도 함께 로그에 포함</li>
     * </ul>
     *
     * @param pjp 진행 중인 조인 포인트(메서드 호출 정보)
     * @return 원본 메서드의 반환 값
     * @throws Throwable 원본 메서드 호출 중 발생한 예외
     */
    @Around("execution(* com.nhnacademy..*Controller.*(..)) || execution(* com.nhnacademy..*Service.*(..))")
    public Object measure(ProceedingJoinPoint pjp) throws Throwable {
        // 메서드 진입 시점 타임스탬프 저장
        long start = System.currentTimeMillis();
        MDC.put("time", String.valueOf(start));

        try {
            // 실제 비즈니스 로직 실행
            return pjp.proceed();
        } finally {
            // 메서드 종료 후 경과 시간 계산
            long rt = System.currentTimeMillis() - start;

            // 로그에 기록할 데이터 구성
            Map<String, Object> m = Map.of(
                    "traceId",       MDC.get("traceId"),
                    "source",        MDC.get("source"),
                    "target",        pjp.getSignature().getDeclaringTypeName() + "#" + pjp.getSignature().getName(),
                    "time",          start,
                    "response_time", rt
            );

            // JSON 형태로 로그 기록
            log.info("{}", m);
            // MDC.clear()는 TraceIdFilter에서 처리하므로 여기서는 호출하지 않음
        }
    }
}
