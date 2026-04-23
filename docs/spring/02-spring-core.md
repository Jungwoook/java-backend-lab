# Spring Core

## 왜 배우는가

Spring Core는 Spring의 중심이다.

Spring MVC, Spring Boot, Spring Data JPA를 사용하더라도 결국 핵심에는 Spring Container와 Bean이 있다. Spring이 객체를 생성하고, 의존성을 연결하고, 생명주기를 관리해주기 때문에 개발자는 비즈니스 로직에 더 집중할 수 있다.

DI / IoC에서 배운 "객체 생성과 연결을 외부에 맡긴다"는 개념이 Spring Core에서 실제 기능으로 구현된다.

## 개념

Spring Container는 애플리케이션에서 사용할 객체를 생성하고 관리하는 공간이다.

Spring이 관리하는 객체를 Bean이라고 부른다.

주요 개념은 다음과 같다.

- Spring Container: Bean을 생성하고 의존 관계를 연결하는 컨테이너
- Bean: Spring Container가 관리하는 객체
- Bean Definition: Bean을 어떻게 만들고 관리할지에 대한 메타정보
- Dependency Injection: Bean 사이의 의존 관계를 주입하는 방식
- Component Scan: 특정 패키지를 탐색해 Bean 후보를 자동으로 등록하는 기능
- Bean Scope: Bean이 생성되고 유지되는 범위
- Bean Lifecycle: Bean 생성, 의존성 주입, 초기화, 사용, 종료 흐름

## 핵심

- Spring Container는 객체 생성과 의존 관계 연결을 담당한다.
- Spring이 관리하는 객체를 Bean이라고 한다.
- `@Configuration`과 `@Bean`을 사용하면 Java 코드로 Bean을 직접 등록할 수 있다.
- `@Component`와 컴포넌트 스캔을 사용하면 Bean을 자동 등록할 수 있다.
- 생성자 주입은 필수 의존성을 명확히 드러내므로 기본 선택으로 적합하다.
- Bean scope의 기본값은 singleton이다.
- Bean lifecycle을 이해하면 초기화와 종료 작업을 올바른 위치에 둘 수 있다.

## 코드 비교

Spring 없이 객체를 직접 생성하면 사용하는 코드가 구체 구현을 선택해야 한다.

```java
class OrderService {
    private final Payment payment = new CardPayment();

    void order(int amount) {
        payment.pay(amount);
    }
}
```

이 방식은 결제 구현을 바꾸려면 `OrderService`를 수정해야 한다.

Spring에서는 객체 생성과 연결을 설정으로 분리할 수 있다.

```java
@Configuration
class AppConfig {

    @Bean
    OrderService orderService() {
        return new OrderService(payment());
    }

    @Bean
    Payment payment() {
        return new CardPayment();
    }
}
```

`OrderService`는 필요한 의존성을 생성자로 받는다.

```java
class OrderService {
    private final Payment payment;

    OrderService(Payment payment) {
        this.payment = payment;
    }

    void order(int amount) {
        payment.pay(amount);
    }
}
```

컴포넌트 스캔을 사용하면 Bean 등록을 더 간단하게 만들 수 있다.

```java
@Service
class OrderService {
    private final Payment payment;

    OrderService(Payment payment) {
        this.payment = payment;
    }
}

@Component
class CardPayment implements Payment {
    public void pay(int amount) {
        System.out.println("카드 결제: " + amount);
    }
}
```

`@Service`와 `@Component`는 이 클래스를 Bean 후보로 등록하라는 표시다. Spring은 생성자를 보고 필요한 `Payment` Bean을 찾아 주입한다.

## 동작 흐름

Spring Container의 기본 동작 흐름은 다음과 같다.

1. Spring이 설정 정보를 읽는다.
2. `@Bean` 또는 `@Component`로 등록할 Bean 정보를 찾는다.
3. Bean 객체를 생성한다.
4. 생성자, setter, 필드 등을 통해 의존성을 주입한다.
5. 초기화 콜백이 있으면 실행한다.
6. 애플리케이션에서 Bean을 사용한다.
7. 컨테이너가 종료될 때 종료 콜백이 있으면 실행한다.

가장 기본적인 흐름은 "설정 읽기 → Bean 생성 → 의존성 주입 → 사용"이다.

## 주의할 점

Bean으로 등록할 객체와 직접 생성할 객체를 구분해야 한다.

Controller, Service, Repository처럼 애플리케이션의 핵심 흐름에서 공유되고 의존성 주입이 필요한 객체는 Bean으로 등록하는 것이 자연스럽다. 반면 요청마다 새로 만들어지는 DTO나 단순 값 객체는 보통 Bean으로 등록하지 않는다.

컴포넌트 스캔을 사용할 때는 패키지 위치도 중요하다. Spring Boot에서는 보통 메인 애플리케이션 클래스가 있는 패키지 아래를 기준으로 컴포넌트 스캔이 동작한다.

또한 singleton Bean은 여러 요청에서 공유될 수 있으므로 상태를 조심해야 한다. Service Bean에 사용자별 데이터를 필드로 저장하면 요청 사이에 데이터가 섞일 위험이 있다.

## 정리

Spring Core의 핵심은 Spring Container와 Bean이다.

Spring Container는 객체를 생성하고, 의존 관계를 연결하고, 생명주기를 관리한다. 개발자는 객체를 직접 조립하는 코드에서 벗어나 역할과 책임에 집중할 수 있다.

`@Configuration`, `@Bean`, `@Component`, `@Service`, 생성자 주입은 Spring Core를 이해하는 가장 중요한 출발점이다.

## 다음으로 연결되는 주제

Spring Core를 이해했다면 다음은 Spring Boot가 이 설정들을 어떻게 단순화하는지 볼 차례다.

다음 주제는 [Spring Boot](03-spring-boot.md)다.
