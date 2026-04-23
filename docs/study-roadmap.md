# Java & Spring Backend Study Roadmap

이 문서는 Java와 Spring 백엔드 스터디의 전체 흐름을 잡기 위한 기준 문서다.

## 학습 방향

- 개념은 Markdown 문서로 정리한다.
- 코드로 확인해야 하는 내용은 `examples` 디렉터리에 예제 코드를 추가한다.
- Java 기초는 이전 학습 내용이 있으므로 핵심만 빠르게 복습한다.
- 객체지향 설계부터는 개념, 설계 이유, 예제, 흔한 실수까지 자세히 다룬다.
- Spring은 DI / IoC 개념을 먼저 잡은 뒤 Spring Core, Spring Boot, MVC, JPA 순서로 확장한다.

## 전체 흐름

### 1. Java 기초

목표: 이후 객체지향 설계와 Spring 학습에 필요한 Java 문법을 빠르게 점검한다.

다룰 내용:

- 클래스, 메서드, 생성자
- 접근 제어자
- 상속, 인터페이스, 추상 클래스의 기본 문법
- 컬렉션
- 예외 처리
- 제네릭
- 람다와 Stream은 필요한 범위에서 복습

정리 방식:

- 이미 알고 있는 문법은 짧게 정리한다.
- 이후 주제와 연결되는 부분만 예제 코드를 추가한다.

### 2. 객체지향 설계

목표: 단순히 문법을 아는 수준을 넘어, 변경에 강한 객체 설계를 이해한다.

다룰 내용:

- [객체와 책임](java/oop-01-object-responsibility.md)
- [캡슐화](java/oop-02-encapsulation.md)
- [응집도와 결합도](java/oop-03-cohesion-coupling.md)
- [다형성과 역할 분리](java/oop-04-polymorphism-role.md)
- [상속보다 조합](java/oop-05-composition-over-inheritance.md)
- [SOLID 원칙](java/oop-06-solid.md)
- [도메인 모델링 기초](java/oop-07-domain-modeling-basic.md)
- [좋은 객체 설계와 나쁜 객체 설계 비교](java/oop-08-good-vs-bad-design.md)

예제 방향:

- 할인 정책, 주문, 결제 같은 작은 도메인으로 설계 변화를 실습한다.
- 처음에는 절차적인 코드로 작성한 뒤 객체지향적으로 개선한다.

### 3. DI / IoC

목표: Spring을 배우기 전에 의존성 주입과 제어의 역전이 왜 필요한지 이해한다.

다룰 내용:

- [DI / IoC](spring/01-di-ioc.md)
- 의존성이란 무엇인가
- 직접 생성 방식의 문제
- 인터페이스와 구현체 분리
- 생성자 주입
- Spring Container가 담당하는 IoC 역할

예제 방향:

- Spring 없이 순수 Java 코드로 DI를 먼저 실습한다.
- 이후 Spring 컨테이너가 같은 문제를 어떻게 해결하는지 연결한다.

### 4. Spring Core

목표: Spring의 핵심인 컨테이너, Bean, 의존성 주입 방식을 이해한다.

다룰 내용:

- [Spring Core](spring/02-spring-core.md)
- Spring Container
- Bean 등록과 조회
- `@Configuration`, `@Bean`
- 컴포넌트 스캔
- `@Component`, `@Service`, `@Repository`, `@Controller`
- 의존성 주입 방식
- Bean scope
- Bean lifecycle

예제 방향:

- Java 설정 기반 Bean 등록부터 시작한다.
- 이후 애노테이션 기반 설정으로 넘어간다.

### 5. Spring Boot

목표: Spring Boot가 기존 Spring 설정을 어떻게 단순화하는지 이해한다.

다룰 내용:

- [Spring Boot](spring/03-spring-boot.md)
- Spring Boot 프로젝트 구조
- 자동 설정
- Starter 의존성
- 내장 톰캣
- `application.yml` / `application.properties`
- Profile
- 테스트 환경 구성

예제 방향:

- 작은 API 프로젝트를 만들며 Boot의 기본 구조를 익힌다.

### 6. MVC

목표: HTTP 요청이 컨트롤러, 서비스, 응답으로 이어지는 흐름을 이해한다.

다룰 내용:

- [MVC](spring/04-mvc.md)
- Web 기본 개념
- Controller
- Request / Response
- DTO
- Validation
- Exception handling
- Layered Architecture
- REST API 설계 기초

예제 방향:

- 간단한 게시글 또는 회원 API를 만든다.
- Controller, Service, Repository 책임을 분리한다.

### 7. JPA

목표: 객체와 관계형 데이터베이스를 연결하는 방식을 이해한다.

다룰 내용:

- [JPA](spring/05-jpa.md)
- ORM 개념
- Entity
- 영속성 컨텍스트
- EntityManager
- Spring Data JPA
- 연관관계 매핑
- 지연 로딩과 즉시 로딩
- 트랜잭션
- N+1 문제
- JPQL

예제 방향:

- 회원, 주문, 상품 같은 관계가 있는 도메인으로 실습한다.
- 단순 CRUD를 넘어서 연관관계와 조회 성능 문제까지 다룬다.

## 문서 작성 규칙

각 주제 문서는 가능하면 다음 흐름을 따른다.

1. 왜 배우는가
2. 핵심 개념
3. 간단한 예제
4. 설계 관점에서 중요한 점
5. 흔한 실수
6. 정리

## 예제 코드 위치

예제 코드는 주제별 디렉터리에 둔다.

```text
examples/
  java/
  oop/
  di/
  spring-core/
  spring-boot/
  mvc/
  jpa/
```

기존 예제는 유지하되, 새 예제는 위 구조에 맞춰 추가한다.

## 우선순위

가장 먼저 자세히 다룰 주제는 객체지향 설계다.

추천 시작 순서:

1. [객체와 책임](java/oop-01-object-responsibility.md)
2. [캡슐화](java/oop-02-encapsulation.md)
3. [응집도와 결합도](java/oop-03-cohesion-coupling.md)
4. [다형성과 역할 분리](java/oop-04-polymorphism-role.md)
5. [상속보다 조합](java/oop-05-composition-over-inheritance.md)
6. [SOLID 원칙](java/oop-06-solid.md)
7. [도메인 모델링 기초](java/oop-07-domain-modeling-basic.md)
8. [좋은 객체 설계와 나쁜 객체 설계 비교](java/oop-08-good-vs-bad-design.md)
9. [DI / IoC로 연결](spring/01-di-ioc.md)
10. [Spring Core](spring/02-spring-core.md)
11. [Spring Boot](spring/03-spring-boot.md)
12. [MVC](spring/04-mvc.md)
13. [JPA](spring/05-jpa.md)
14. [Spring Annotation 요약](spring/06-spring-annotations.md)
