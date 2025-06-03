package com.nhnacademy.traceloggermodule.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;

/**
 * 💡 logback-spring.xml 대신 동적으로 logback XML 설정을 Java 코드로 로딩
 * - 공통 로깅 설정을 trace-logger-module에 포함시켜 모든 서비스에 적용 가능
 */
@Slf4j
@Configuration
public class DynamicLogbackLoader {

    @Value("${logging.file.path:./logs}")
    private String logPath;

    @PostConstruct
    public void loadLogback() {
        try {
            LoggerContext context = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
            context.reset();

            context.putProperty("LOG_PATH", logPath); // 변수 설정

            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);

            InputStream configStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("logback-shared.xml");

            if (configStream == null) {
                throw new IllegalStateException("logback-shared.xml not found in classpath");
            }

            configurator.doConfigure(configStream);
            log.info("✅ Loaded logback-shared.xml successfully from trace-logger-module");

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to load logback configuration from module", e);
        }
    }
}
