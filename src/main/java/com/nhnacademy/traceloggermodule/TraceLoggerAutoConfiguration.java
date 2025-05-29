package com.nhnacademy.traceloggermodule;

import com.nhnacademy.traceloggermodule.config.TraceIdFilter;
import com.nhnacademy.traceloggermodule.logging.FlowLoggingAspect;
import com.nhnacademy.traceloggermodule.logging.ResponseTimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 🧩 Spring Boot Auto Configuration 구성
 * - 사용자가 별도 Bean 등록하지 않아도 자동 적용되도록 설정
 * - 관심사 분리를 지키면서 유연한 모듈화 가능
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)

public class TraceLoggerAutoConfiguration {

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public FlowLoggingAspect flowLoggingAspect() {
        return new FlowLoggingAspect();
    }

    @Bean
    public ResponseTimeAspect responseTimeAspect() {
        return new ResponseTimeAspect();
    }
}
