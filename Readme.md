# trace-logger-module

MSA 환경에서 서비스 간 흐름 추적을 위해 사용하는 **Spring Boot 공통 로거 모듈**입니다.
traceId를 자동 생성/전파하고, 서비스 호출 흐름을 자동으로 구조화된 로그로 기록합니다.

---

##  주요 기능

-  `traceId`, `source` 자동 생성 및 MDC 설정
-  AOP 기반 `target` 흐름 로그 자동 기록
-  Feign 호출 시 traceId 전파 지원
-  Spring Boot Auto Configuration으로 자동 적용
-  Kibana 등에서 흐름 시각화 가능 (Sankey, Time Series 등)

---

##  사용 방법

### 1. JitPack 의존성 추가

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.dusen0528</groupId>
  <artifactId>trace-logger-module</artifactId>
  <version>1.0.0</version>
</dependency>
```

### 2. 별도 설정 없이 동작합니다
TraceIdFilter: 모든 요청에 traceId + source 주입

FlowLoggingAspect: Service/Controller 진입 시 흐름 자동 로깅

FlowLogger: 수동으로 특정 위치에서 흐름 기록 가능

### 3. application.properties에 서비스명 명시 (data-processor-service 예시)
```angular2html
spring.application.name=data-processor-service

```
### 로그 예시
```
{
  "traceId": "5af1-dcf0...",
  "source": "gateway-service",
  "target": "com.nhnacademy.member.MemberService#create",
  "timestamp": "2025-05-14T09:00:00Z"
}
```

### 디렉토리 구조
com.nhnacademy.traceloggermodule
├── config
│   ├── TraceIdFilter.java
│   └── FeignTraceInterceptor.java
├── logging
│   ├── FlowLogger.java
│   └── FlowLoggingAspect.java
├── TraceLoggerAutoConfiguration.java
└── resources/META-INF/spring.factories


### 활용방안
```angular2html
┌──────────────┐       traceId 생성        ┌──────────────┐
│  gateway     │─────────────────────────▶│  member      │
└──────────────┘        (자동 로그)        └──────────────┘
      │                                        │
      ▼                                        ▼
 target: AuthFilter#doFilter      target: MemberService#create
```

