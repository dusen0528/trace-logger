package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.Map;

/**
 *  AOP 외 커스텀 위치에서도
 * traceId 기반 흐름 로그를 남길 때 사용하는 유틸.
 */
@Slf4j
public class FlowLogger {

    /**
     * @param target  호출 지점 식별자 (e.g. Class#method)
     * @param payload 선택적 페이로드 객체 (null 가능)
     */
    public static void log(String target, Object payload) {
        Map<String, Object> flow = Map.of(
                "traceId", MDC.get("traceId"),
                "source", MDC.get("source"),
                "target", target,
                "@timestamp", Instant.now().toString(),
                "payload", payload
        );
        log.info("trace-flow", StructuredArguments.entries(flow));
    }
}
