# Spring Boot

## 왜 배우는가

Spring은 강력하지만 설정해야 할 것이 많다.

Spring Boot는 Spring 애플리케이션을 더 빠르게 만들고 실행할 수 있도록 도와준다. 반복적인 설정을 줄이고, 자주 사용하는 라이브러리 조합을 Starter로 제공하며, 내장 서버를 통해 별도의 WAS 설치 없이 애플리케이션을 실행할 수 있게 해준다.

Spring Boot를 이해하면 "왜 설정이 자동으로 되는지", "어디까지 자동 설정에 맡기고 어디부터 직접 설정해야 하는지"를 판단할 수 있다.

## 개념

Spring Boot는 Spring 기반 애플리케이션을 빠르게 만들기 위한 도구와 설정 묶음이다.

주요 개념은 다음과 같다.

- Auto Configuration: classpath와 설정값을 보고 필요한 Bean을 자동으로 구성한다.
- Starter: 특정 기능에 필요한 의존성 묶음이다.
- Embedded Server: Tomcat 같은 서버를 애플리케이션 안에 포함해 실행한다.
- Externalized Configuration: `application.yml` 또는 `application.properties`로 설정을 외부화한다.
- Profile: 환경별 설정을 분리한다.
- Actuator: 애플리케이션 상태 확인과 운영 정보를 제공한다.

## 핵심

- Spring Boot는 Spring 설정을 없애는 것이 아니라 합리적인 기본값을 제공한다.
- `spring-boot-starter-*`는 관련 라이브러리들을 묶어준다.
- `@SpringBootApplication`은 여러 핵심 애노테이션을 포함한다.
- 내장 서버 덕분에 `java -jar` 방식으로 애플리케이션을 실행할 수 있다.
- 환경별 설정은 Profile로 분리할 수 있다.
- 자동 설정을 이해하면 문제가 생겼을 때 원인을 찾기 쉬워진다.

## 코드 비교

Spring Boot 애플리케이션의 시작점은 보통 다음처럼 생겼다.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

`@SpringBootApplication`은 대표적으로 다음 기능을 포함한다.

```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
class Application {
}
```

즉, Spring Boot 애플리케이션은 설정 클래스이면서, 자동 설정을 활성화하고, 컴포넌트 스캔을 수행한다.

설정값은 코드에 직접 박아두지 않고 외부 설정 파일로 분리할 수 있다.

```yaml
server:
  port: 8080

spring:
  application:
    name: java-backend-lab
```

환경별 설정은 Profile로 나눌 수 있다.

```yaml
spring:
  config:
    activate:
      on-profile: local

logging:
  level:
    org.springframework: debug
```

## 동작 흐름

Spring Boot 애플리케이션은 대략 다음 흐름으로 실행된다.

1. `main()` 메서드에서 `SpringApplication.run()`을 호출한다.
2. Spring Boot가 애플리케이션 환경과 설정 파일을 읽는다.
3. 컴포넌트 스캔으로 Bean 후보를 찾는다.
4. classpath와 조건에 따라 자동 설정을 적용한다.
5. Spring Container를 만들고 Bean을 등록한다.
6. 내장 서버를 실행한다.
7. 애플리케이션이 요청을 받을 준비를 마친다.

핵심은 Spring Boot가 Spring Container 생성 전후에 필요한 기본 설정을 자동으로 준비한다는 점이다.

## 주의할 점

자동 설정은 편리하지만 마법처럼 이해하면 안 된다.

어떤 Bean이 자동 등록되었는지, 어떤 설정값이 적용되었는지 모르면 문제가 생겼을 때 원인을 찾기 어렵다. Spring Boot는 기본값을 제공하지만 직접 설정한 Bean이나 프로퍼티가 있으면 그 설정이 우선될 수 있다.

또한 Profile 설정을 사용할 때는 현재 어떤 Profile이 활성화되어 있는지 명확히 확인해야 한다. local, dev, prod 설정이 섞이면 DB 연결이나 외부 API 호출에서 큰 문제가 생길 수 있다.

## 정리

Spring Boot는 Spring 애플리케이션을 빠르게 만들고 실행할 수 있게 해준다.

자동 설정, Starter, 내장 서버, 외부 설정, Profile은 Spring Boot의 핵심 기능이다. Spring Boot를 잘 사용하려면 자동으로 되는 부분을 믿되, 필요할 때 직접 추적할 수 있어야 한다.

## 다음으로 연결되는 주제

Spring Boot로 애플리케이션을 실행할 수 있게 되면, 다음은 HTTP 요청을 받아 응답하는 구조를 배워야 한다.

다음 주제는 [MVC](04-mvc.md)다.
