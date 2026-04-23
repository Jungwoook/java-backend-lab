# SOLID 원칙

## 왜 배우는가

SOLID는 객체지향 설계를 할 때 자주 등장하는 다섯 가지 원칙이다.

원칙을 외우는 것 자체가 목적은 아니다. 중요한 것은 변경에 강한 구조를 만들기 위해 어떤 방향으로 코드를 나누고 의존해야 하는지 이해하는 것이다.

Spring을 공부할 때도 SOLID는 계속 연결된다. 특히 DIP는 DI / IoC와 직접 이어지고, OCP는 인터페이스와 다형성을 사용하는 이유를 설명해준다.

## 개념

SOLID는 다음 다섯 가지 원칙의 앞 글자를 모은 이름이다.

- SRP: Single Responsibility Principle, 단일 책임 원칙
- OCP: Open Closed Principle, 개방 폐쇄 원칙
- LSP: Liskov Substitution Principle, 리스코프 치환 원칙
- ISP: Interface Segregation Principle, 인터페이스 분리 원칙
- DIP: Dependency Inversion Principle, 의존관계 역전 원칙

각 원칙은 서로 떨어져 있는 규칙이 아니라, 변경하기 쉬운 객체 구조를 만들기 위해 함께 사용된다.

## 핵심

- SRP: 하나의 클래스는 하나의 변경 이유를 가지는 것이 좋다.
- OCP: 기존 코드를 수정하기보다 새로운 구현을 추가해 확장할 수 있어야 한다.
- LSP: 자식 타입은 부모 타입을 대체해도 프로그램의 의미가 깨지지 않아야 한다.
- ISP: 클라이언트는 자신이 사용하지 않는 메서드에 의존하지 않아야 한다.
- DIP: 고수준 정책은 저수준 구현이 아니라 추상화에 의존해야 한다.

## 코드 비교

### SRP: 단일 책임 원칙

다음 코드는 주문 생성과 알림 발송 책임이 한 클래스에 함께 있다.

```java
class OrderService {
    void order(String productName) {
        System.out.println(productName + " 주문 생성");
        System.out.println("주문 완료 알림 발송");
    }
}
```

알림 방식이 바뀌어도 `OrderService`가 수정된다. 주문 책임과 알림 책임을 분리하면 변경 이유가 줄어든다.

```java
class OrderService {
    private final OrderNotifier notifier;

    OrderService(OrderNotifier notifier) {
        this.notifier = notifier;
    }

    void order(String productName) {
        System.out.println(productName + " 주문 생성");
        notifier.notifyOrderCompleted(productName);
    }
}

class OrderNotifier {
    void notifyOrderCompleted(String productName) {
        System.out.println(productName + " 주문 완료 알림 발송");
    }
}
```

### OCP: 개방 폐쇄 원칙

다음 코드는 할인 정책이 늘어날 때마다 `DiscountService`의 조건문이 계속 수정된다.

```java
class DiscountService {
    int discount(String grade, int price) {
        if (grade.equals("VIP")) {
            return price - 1000;
        }

        return price;
    }
}
```

정책을 인터페이스로 분리하면 기존 서비스 코드를 크게 바꾸지 않고 새 할인 정책을 추가할 수 있다.

```java
interface DiscountPolicy {
    int discount(int price);
}

class VipDiscountPolicy implements DiscountPolicy {
    public int discount(int price) {
        return price - 1000;
    }
}

class DiscountService {
    private final DiscountPolicy discountPolicy;

    DiscountService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    int discount(int price) {
        return discountPolicy.discount(price);
    }
}
```

### LSP: 리스코프 치환 원칙

부모 타입으로 사용할 수 있다고 해서 항상 올바른 상속은 아니다.

```java
class Bird {
    void fly() {
        System.out.println("난다");
    }
}

class Penguin extends Bird {
    @Override
    void fly() {
        throw new UnsupportedOperationException("펭귄은 날 수 없다");
    }
}
```

`Bird`를 사용하는 코드는 모든 새가 날 수 있다고 기대한다. 그런데 `Penguin`이 들어오면 프로그램의 의미가 깨진다.

```java
interface Bird {
}

interface Flyable {
    void fly();
}

class Sparrow implements Bird, Flyable {
    public void fly() {
        System.out.println("참새가 난다");
    }
}

class Penguin implements Bird {
}
```

날 수 있는 능력을 별도 역할로 분리하면 `Flyable`을 사용하는 코드는 실제로 날 수 있는 객체만 받게 된다.

### ISP: 인터페이스 분리 원칙

너무 큰 인터페이스는 구현 클래스가 사용하지 않는 기능까지 억지로 의존하게 만든다.

```java
interface Worker {
    void work();
    void eat();
}

class RobotWorker implements Worker {
    public void work() {
        System.out.println("로봇 작업");
    }

    public void eat() {
        throw new UnsupportedOperationException("로봇은 먹지 않는다");
    }
}
```

역할별로 인터페이스를 나누면 필요한 기능에만 의존할 수 있다.

```java
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class RobotWorker implements Workable {
    public void work() {
        System.out.println("로봇 작업");
    }
}

class HumanWorker implements Workable, Eatable {
    public void work() {
        System.out.println("사람 작업");
    }

    public void eat() {
        System.out.println("식사");
    }
}
```

### DIP: 의존관계 역전 원칙

다음 코드는 주문 서비스가 구체 결제 구현에 직접 의존한다.

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

이 구조는 결제 방식이 바뀌면 `OrderService`가 수정된다. OCP와 DIP를 지키기 어렵다.

추상화에 의존하도록 바꾸면 다음과 같다.

```java
interface Payment {
    void pay(int amount);
}

class CardPayment implements Payment {
    public void pay(int amount) {
        System.out.println("카드 결제: " + amount);
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

이제 `OrderService`는 결제 역할에만 의존한다. 새로운 결제 방식이 필요하면 `Payment` 구현체를 추가하면 된다.

## 동작 흐름

SOLID를 적용한 설계 흐름은 보통 다음과 같다.

1. 변경 이유가 다른 책임을 분리한다.
2. 바뀔 가능성이 높은 부분을 인터페이스로 추상화한다.
3. 사용하는 코드는 구체 구현이 아니라 추상화에 의존한다.
4. 새로운 기능은 기존 코드를 크게 수정하지 않고 구현체 추가로 확장한다.

이 흐름은 이후 Spring DI가 객체를 연결하는 방식과 자연스럽게 이어진다.

## 주의할 점

SOLID를 처음부터 완벽하게 적용하려고 하면 코드가 불필요하게 복잡해질 수 있다.

모든 클래스에 인터페이스를 만들거나, 모든 책임을 지나치게 잘게 나누는 것은 좋은 설계가 아니다. 원칙은 코드를 복잡하게 만들기 위한 규칙이 아니라 변경을 다루기 위한 기준이다.

원칙을 적용할 때는 다음 질문을 함께 생각하는 것이 좋다.

- 이 코드는 어떤 이유로 바뀔까?
- 바뀌는 부분과 안정적인 부분이 분리되어 있는가?
- 사용하는 코드가 구체 구현을 너무 많이 알고 있지는 않은가?

## 정리

SOLID는 객체지향 설계를 점검하는 대표적인 기준이다.

단일 책임으로 변경 이유를 줄이고, 다형성과 추상화로 확장 가능성을 높이며, 의존 방향을 안정적인 쪽으로 조정한다. 핵심은 원칙 이름을 외우는 것이 아니라 변경에 강한 구조를 만드는 것이다.

## 다음으로 연결되는 주제

SOLID를 실제 코드에 적용하려면 도메인 개념을 객체로 잘 나누는 연습이 필요하다.

다음 주제는 도메인 모델링 기초다.
