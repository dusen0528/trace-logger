package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.Map;
import net.logstash.logback.argument.StructuredArguments;

/**
 *   AOP 외의 커스텀 위치에서도 흐름 로그를 남기고자 할 때 사용하는 유틸 클래스
 * - 예: 메시지 큐 소비, 비동기 스레드 등
 */
@Slf4j
public class FlowLogger {

    public static void log(String target, Object payload) {
        Map<String, Object> flow = Map.of(
                "traceId", MDC.get("traceId"),
                "source", MDC.get("source"),
                "target", target,
                "@timestamp", Instant.now().toString()
        );

        log.info("trace-flow", StructuredArguments.entries(flow));
    }
}