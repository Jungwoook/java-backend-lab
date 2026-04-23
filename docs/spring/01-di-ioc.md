# DI / IoC

## 왜 배우는가

Spring을 제대로 이해하려면 먼저 DI와 IoC를 이해해야 한다.

Spring은 객체를 대신 생성하고, 필요한 객체끼리 연결해준다. 이 기능을 단순히 편리한 자동 생성 기능으로만 보면 Spring의 핵심을 놓치기 쉽다. 중요한 것은 객체가 직접 의존 대상을 만들지 않고, 외부에서 필요한 의존성을 전달받는 구조다.

DI와 IoC를 이해하면 객체지향 설계에서 배운 다형성, 역할 분리, DIP가 Spring에서 어떻게 구현되는지 자연스럽게 연결된다.

## 개념

DI는 Dependency Injection의 줄임말로, 의존성 주입이라고 부른다.

의존성이란 어떤 객체가 동작하기 위해 다른 객체를 필요로 하는 관계다.

```java
class OrderService {
    private final Payment payment;

    OrderService(Payment payment) {
        this.payment = payment;
    }
}
```

위 코드에서 `OrderService`는 결제를 처리하기 위해 `Payment`가 필요하다. 따라서 `OrderService`는 `Payment`에 의존한다.

DI는 이 의존 객체를 클래스 내부에서 직접 만들지 않고 외부에서 전달받는 방식이다.

IoC는 Inversion of Control의 줄임말로, 제어의 역전이라고 부른다.

일반적인 코드에서는 객체가 자신에게 필요한 객체를 직접 생성하고 제어한다. 반면 IoC 구조에서는 객체 생성과 연결의 제어권이 외부로 넘어간다. Spring에서는 Spring Container가 이 역할을 맡는다.

## 핵심

- 의존성은 한 객체가 다른 객체를 필요로 하는 관계다.
- DI는 필요한 의존 객체를 내부에서 직접 만들지 않고 외부에서 전달받는 방식이다.
- IoC는 객체 생성과 의존 관계 연결의 제어권이 객체 자신이 아니라 외부에 있는 구조다.
- DI를 사용하면 구체 구현이 아니라 인터페이스에 의존하기 쉬워진다.
- Spring Container는 객체를 생성하고 의존성을 주입해주는 IoC Container다.
- 생성자 주입은 의존성을 명확하게 드러내므로 가장 권장되는 방식이다.

## 코드 비교

다음 코드는 `OrderService`가 `CardPayment`를 직접 생성한다.

```java
class CardPayment {
    void pay(int amount) {
        System.out.println("카드 결제: " + amount);
    }
}

class OrderService {
    private final CardPayment payment = new CardPayment();

    void order(int amount) {
        payment.pay(amount);
    }
}
```

이 구조에서는 결제 방식을 바꾸려면 `OrderService` 코드를 수정해야 한다. `OrderService`가 구체 클래스인 `CardPayment`에 강하게 묶여 있기 때문이다.

인터페이스와 생성자 주입을 사용하면 다음처럼 바꿀 수 있다.

```java
interface Payment {
    void pay(int amount);
}

class CardPayment implements Payment {
    public void pay(int amount) {
        System.out.println("카드 결제: " + amount);
    }
}

class BankTransferPayment implements Payment {
    public void pay(int amount) {
        System.out.println("계좌 이체 결제: " + amount);
    }
}

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

이제 `OrderService`는 어떤 결제 구현체가 들어오는지 직접 결정하지 않는다. 외부에서 `Payment` 구현체를 전달받아 사용할 뿐이다.

객체를 조립하는 코드는 다음처럼 별도로 둘 수 있다.

```java
class AppConfig {
    OrderService orderService() {
        return new OrderService(payment());
    }

    Payment payment() {
        return new CardPayment();
    }
}
```

`AppConfig`는 객체 생성과 연결을 담당한다. `OrderService`는 주문 흐름에 집중한다.

## 동작 흐름

Spring 없이 순수 Java로 보면 DI / IoC 흐름은 다음과 같다.

1. `AppConfig`가 `Payment` 구현체를 생성한다.
2. `AppConfig`가 `OrderService`를 생성하면서 `Payment`를 생성자로 전달한다.
3. `OrderService`는 전달받은 `Payment`를 필드에 보관한다.
4. 주문이 들어오면 `OrderService`는 `payment.pay(amount)`를 호출한다.
5. 실제 결제 방식은 주입된 구현체에 따라 결정된다.

Spring에서는 이 `AppConfig` 역할을 Spring Container가 더 체계적으로 수행한다.

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

여기서 `@Bean`으로 등록된 객체는 Spring Container가 관리한다. Spring Container는 필요한 객체를 생성하고, 의존 관계를 연결해준다.

## 주의할 점

DI를 사용한다고 해서 무조건 인터페이스를 만들어야 하는 것은 아니다.

구현이 하나뿐이고 바뀔 가능성이 낮은 객체라면 구체 클래스에 의존해도 괜찮을 수 있다. 하지만 결제 방식, 할인 정책, 알림 방식, 외부 API 연동처럼 구현이 바뀔 가능성이 높은 부분은 인터페이스로 역할을 분리하는 것이 좋다.

또한 DI는 단순히 `new`를 없애는 기술이 아니다. 중요한 것은 객체가 자신의 핵심 책임에 집중하고, 객체 생성과 연결 책임을 외부로 분리하는 것이다.

필드 주입은 테스트와 변경에 불리할 수 있으므로 학습 초기에는 생성자 주입을 기본으로 생각하는 것이 좋다.

```java
class OrderService {
    private final Payment payment;

    OrderService(Payment payment) {
        this.payment = payment;
    }
}
```

생성자 주입을 사용하면 객체가 생성될 때 필요한 의존성이 명확하게 드러난다.

## 정리

DI는 객체가 필요한 의존성을 외부에서 전달받는 방식이다.

IoC는 객체 생성과 의존 관계 연결의 제어권이 객체 자신이 아니라 외부로 넘어가는 구조다. Spring에서는 Spring Container가 객체를 생성하고 의존성을 주입하는 IoC Container 역할을 한다.

DI / IoC를 사용하면 객체는 자신의 책임에 집중하고, 객체 사이의 연결은 외부 설정이나 컨테이너가 담당할 수 있다.

## 다음으로 연결되는 주제

DI / IoC를 이해했다면 다음은 Spring이 객체를 어떻게 생성하고 관리하는지 살펴볼 차례다.

다음 주제는 [Spring Core](02-spring-core.md)다. Spring Core에서는 Spring Container, Bean, `@Configuration`, `@Bean`, 컴포넌트 스캔을 다룬다.
