package com.nhnacademy.traceloggermodule.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

/**
 * 🌐 OpenFeign 요청 시
 * MDC의 traceId, source를 HTTP 헤더에 전파합니다.
 */
public class FeignTraceInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String traceId = MDC.get("traceId");
        String source  = MDC.get("source");
        if (traceId != null) template.header("X-Trace-Id", traceId);
        if (source  != null) template.header("X-Source", source);
    }
}
