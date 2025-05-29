package com.nhnacademy.traceloggermodule;

import com.nhnacademy.traceloggermodule.config.TraceIdAspect;
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
 * 🧩 자동 구성 클래스
 * - HTTP Filter, TraceId AOP, Flow/ResponseTime AOP, Feign 인터셉터를
 *   별도 설정 없이 빈으로 등록합니다.
 */
@AutoConfiguration
@ConditionalOnClass(AnnotationAwareAspectJAutoProxyCreator.class)
@EnableFeignClients
public class TraceLoggerAutoConfiguration {

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration(TraceIdFilter filter) {
        FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean @ConditionalOnMissingBean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean @ConditionalOnMissingBean
    public TraceIdAspect traceIdAspect() {
        return new TraceIdAspect();
    }

    @Bean @ConditionalOnMissingBean
    public FlowLoggingAspect flowLoggingAspect() {
        return new FlowLoggingAspect();
    }

    @Bean @ConditionalOnMissingBean
    public ResponseTimeAspect responseTimeAspect() {
        return new ResponseTimeAspect();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignClient")
    @ConditionalOnMissingBean
    public RequestInterceptor feignTraceInterceptor() {
        return new FeignTraceInterceptor();
    }
}
