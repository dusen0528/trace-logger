package com.nhnacademy.traceloggermodule.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * ✍  AOP 외의 커스텀 위치에서도 흐름 로그를 남기고자 할 때 사용하는 유틸 클래스
 * - 예: 메시지 큐 소비, 비동기 스레드 등
 */
@Slf4j
public class FlowLogger {

    public static void log(String target, Map<String, Object> payload) {
        Map<String, Object> logData = new HashMap<>(payload);
        logData.put("traceId", MDC.get("traceId"));
        logData.put("source", MDC.get("source"));
        logData.put("target", target);
        logData.put("timestamp", Instant.now().toString());

        log.info("{}", logData);
    }
}