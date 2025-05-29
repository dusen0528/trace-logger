package com.nhnacademy.traceloggermodule;

import com.nhnacademy.traceloggermodule.config.TraceIdFilter;
import com.nhnacademy.traceloggermodule.config.FeignTraceInterceptor;
import com.nhnacademy.traceloggermodule.logging.FlowLoggingAspect;
import com.nhnacademy.traceloggermodule.logging.ResponseTimeAspect;
import feign.RequestInterceptor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 *   자동 구성 클래스
 * - AOP, TraceId 필터, Flow/ResponseTime Aspect, Feign 인터셉터를
 *   별도 설정 없이 빈으로 등록해준다.
 * - Web 애플리케이션에서만 TraceIdFilter를 FilterRegistrationBean으로 등록.
 */
@AutoConfiguration
@ConditionalOnClass(AnnotationAwareAspectJAutoProxyCreator.class)
@EnableFeignClients
public class TraceLoggerAutoConfiguration {

    /**
     * HTTP 요청마다 traceId/MDC를 주입하는 필터를 최우선 순위로 등록한다.
     */
    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration(TraceIdFilter filter) {
        FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    /**
     * TraceIdFilter 빈 생성.
     * @return TraceIdFilter
     */
    @Bean
    @ConditionalOnMissingBean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    /**
     * 서비스/컨트롤러 진입 시 흐름 로그를 남기는 Aspect.
     * @return FlowLoggingAspect
     */
    @Bean
    @ConditionalOnMissingBean
    public FlowLoggingAspect flowLoggingAspect() {
        return new FlowLoggingAspect();
    }

    /**
     * 메서드 실행 시간을 측정해 MDC에 response_time을 추가하는 Aspect.
     * @return ResponseTimeAspect
     */
    @Bean
    @ConditionalOnMissingBean
    public ResponseTimeAspect responseTimeAspect() {
        return new ResponseTimeAspect();
    }

    /**
     * OpenFeign 호출 시 traceId/header를 전파하는 인터셉터.
     * @return FeignTraceInterceptor
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignClient")
    @ConditionalOnMissingBean
    public RequestInterceptor feignTraceInterceptor() {
        return new FeignTraceInterceptor();
    }
}
