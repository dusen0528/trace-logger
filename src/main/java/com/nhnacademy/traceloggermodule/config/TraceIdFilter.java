package com.nhnacademy.traceloggermodule.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 *   HTTP 요청에 한해 동작하는 Filter.
 * - 요청 헤더 'X-Trace-Id'가 있으면 재사용, 없으면 새로 생성
 * - MDC에 traceId, source(서비스명) 주입
 * - 응답 헤더에도 X-Trace-Id 설정
 * - HTTP 요청 종료 시 MDC.clear()로 클리어
 */
public class TraceIdFilter extends OncePerRequestFilter {

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put("traceId", traceId);
        MDC.put("source", serviceName);
        response.setHeader("X-Trace-Id", traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
