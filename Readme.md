# trace-logger-module

MSA 환경에서 서비스 간 호출 흐름을 손쉽게 추적하기 위한 **Spring Boot 공통 로깅 모듈**입니다. 
`traceId`와 서비스 이름(`source`)을 자동으로 생성해 MDC와 HTTP 헤더에 주입하고, AOP 기반으로 흐름·응답시간 로그를 남깁니다. 별도 설정 없이 의존성 추가만으로 공통 로깅 체계를 적용할 수 있습니다.

---

## 주요 구성

```
src/main/java/com/nhnacademy/traceloggermodule
├── config
│   ├── TraceIdFilter.java        # HTTP 요청마다 traceId 생성·전파
│   ├── TraceIdAspect.java        # 비 HTTP 진입 지점 traceId 부여
│   └── FeignTraceInterceptor.java# Feign 호출 시 trace 정보 전달
├── logging
│   ├── FlowLogger.java           # 커스텀 위치에서 흐름 로그 남길 때 사용
│   ├── FlowLoggingAspect.java    # Service/Controller 진입 로그
│   ├── ResponseTimeAspect.java   # 메서드 실행 시간 측정
│   └── DynamicLogbackLoader.java # logback-shared.xml 로드
└── TraceLoggerAutoConfiguration.java
```

---

## 사용 방법

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

* 버전의 경우 https://jitpack.io/#dusen0528/trace-logger <-- 확인 가능합니다 *

### 2. application.properties 설정
서비스 이름을 로그의 `source` 필드로 사용하므로 반드시 `spring.application.name` 값을 지정합니다.
```properties
spring.application.name=data-processor-service
```

### 3. 별도 코드 없이 자동 동작
모듈을 의존성으로 추가하면 아래 컴포넌트가 자동으로 등록됩니다.
- **TraceIdFilter**: 모든 HTTP 요청에 traceId와 source 주입
- **TraceIdAspect**: HTTP 외 호출에도 traceId 부여 및 정리
- **FlowLoggingAspect**: Service/Controller 진입 시 `trace-flow` 로그
- **ResponseTimeAspect**: 메서드 실행 시간 `response-time` 로그
- **FeignTraceInterceptor**: OpenFeign 호출 시 traceId 전파
- **DynamicLogbackLoader**: 모듈 내 `logback-shared.xml` 로딩

필요 시 `FlowLogger.log()`를 통해 원하는 위치에서 추가 흐름 로그를 남길 수 있습니다.

### 4. 로그 예시
```
{
  "traceId": "5af1-dcf0...",
  "source": "gateway-service",
  "target": "com.nhnacademy.member.MemberService#create",
  "@timestamp": "2025-05-14T09:00:00Z"
}
```

---

## 알아 두어야 할 사항
1. Java 21, Spring Boot 3.4.4 환경을 기준으로 빌드됩니다.
2. 공통 `logback-shared.xml`이 자동 적용되며 로그 파일 경로는 `logging.file.path`로 지정할 수 있습니다.
3. Kibana와 같은 도구에서 traceId로 서비스를 연결해 요청 흐름을 시각화할 수 있습니다.

### 더 학습하면 좋은 내용
- Spring AOP와 Filter 동작 원리
- Logback 설정 커스터마이징 방법
- OpenFeign 사용법 및 MDC(Mapped Diagnostic Context) 활용법

---

이 모듈을 활용하면 여러 마이크로서비스에서 일관된 형식의 흐름 로그를 남기고, traceId를 기반으로 손쉽게 호출 관계를 파악할 수 있습니다.
