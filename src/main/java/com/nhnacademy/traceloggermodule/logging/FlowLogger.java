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

    public static void log(String target, Object payload) {
        String traceId = MDC.get("traceId");
        String source  = MDC.get("source");

        Map<String, Object> flow = new java.util.HashMap<>();
        flow.put("traceId", traceId != null ? traceId : "MISSING");
        flow.put("source", source != null ? source : "MISSING");
        flow.put("target", target);
        flow.put("@timestamp", Instant.now().toString());
        if (payload != null) {
            flow.put("payload", payload);
        }

        log.info("trace-flow", StructuredArguments.entries(flow));
    }
}
