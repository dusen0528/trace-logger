package com.nhnacademy.traceloggermodule.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

/**
 *   FeignClient를 사용할 때 traceId, source를 HTTP Header로 자동 전파하는 설정
 * - Spring Cloud OpenFeign에 최적화됨
 * - 서비스 간 호출에서도 흐름을 유지 가능하게 함
 */
public class FeignTraceInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String traceId = MDC.get("traceId");
        String source = MDC.get("source");

        if (traceId != null) template.header("X-Trace-Id", traceId);
        if (source != null) template.header("X-Source", source);
    }
}
