# 다형성과 역할 분리

## 왜 배우는가

객체지향 설계에서 변경에 강한 코드를 만들려면 구체적인 구현보다 역할에 의존해야 한다.

결제 기능을 예로 들면 처음에는 카드 결제만 필요할 수 있다. 하지만 나중에 계좌 이체, 포인트 결제, 간편 결제가 추가될 수 있다. 이때 주문 코드가 카드 결제 클래스에 직접 의존하고 있으면 새로운 결제 방식을 추가할 때 주문 코드도 함께 바뀐다.

다형성은 같은 역할을 여러 구현으로 대체할 수 있게 해준다.

## 개념

다형성은 같은 메시지를 받은 객체들이 각자의 방식으로 동작할 수 있는 성질이다.

Java에서는 보통 인터페이스나 추상 클래스를 통해 다형성을 표현한다.

- 역할: 사용하는 쪽이 기대하는 기능의 약속
- 구현: 역할을 실제로 수행하는 구체 클래스
- 다형성: 같은 역할을 가진 여러 구현을 바꿔 끼울 수 있는 성질

객체지향에서 중요한 것은 "누가 이 일을 할 수 있는가?"를 역할로 정의하고, 실제 수행 방식은 구현 객체에게 맡기는 것이다.

## 핵심

- 역할과 구현을 분리하면 변경에 강해진다.
- 사용하는 코드는 구체 클래스보다 인터페이스에 의존하는 것이 좋다.
- 다형성은 조건문을 줄이고 구현 교체를 쉽게 만든다.
- 새로운 구현이 추가되어도 기존 코드를 덜 수정하게 된다.
- Spring의 DI는 역할과 구현 분리를 자연스럽게 연결해준다.

## 코드 비교

다음 코드는 주문 서비스가 카드 결제 구현에 직접 의존한다.

```java
class CardPayment {
    void pay(int amount) {
        System.out.println("카드로 " + amount + "원 결제");
    }
}

class OrderService {
    private final CardPayment cardPayment = new CardPayment();

    void order(int amount) {
        cardPayment.pay(amount);
    }
}
```

계좌 이체 결제를 추가하려면 `OrderService`도 수정해야 한다.

역할을 인터페이스로 분리하면 다음처럼 바꿀 수 있다.

```java
interface Payment {
    void pay(int amount);
}

class CardPayment implements Payment {
    public void pay(int amount) {
        System.out.println("카드로 " + amount + "원 결제");
    }
}

class BankTransferPayment implements Payment {
    public void pay(int amount) {
        System.out.println("계좌 이체로 " + amount + "원 결제");
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

`OrderService`는 이제 카드 결제인지 계좌 이체인지 알 필요가 없다. `Payment` 역할만 알고 결제를 요청한다.

## 동작 흐름

1. `OrderService`는 생성될 때 `Payment` 구현체를 전달받는다.
2. 주문이 들어오면 `OrderService`는 `payment.pay(amount)`를 호출한다.
3. 실제 객체가 `CardPayment`이면 카드 결제가 실행된다.
4. 실제 객체가 `BankTransferPayment`이면 계좌 이체 결제가 실행된다.

호출하는 코드는 같지만 실제 동작은 객체에 따라 달라진다. 이것이 다형성이다.

## 주의할 점

인터페이스를 무조건 먼저 만드는 것이 좋은 설계는 아니다.

구현이 하나뿐이고 변경 가능성이 거의 없다면 인터페이스가 오히려 불필요한 복잡도를 만들 수 있다. 하지만 결제, 할인 정책, 알림 방식처럼 구현이 바뀔 가능성이 높은 부분은 역할을 분리하는 것이 좋다.

또한 인터페이스는 너무 많은 메서드를 가지면 안 된다. 역할이 커질수록 구현 클래스가 필요 없는 기능까지 억지로 구현하게 된다.

## 정리

다형성은 같은 역할을 여러 구현으로 대체할 수 있게 해준다.

역할과 구현을 분리하면 사용하는 코드는 안정되고, 변경되는 구현은 독립적으로 바꿀 수 있다. 이 구조는 이후 DI / IoC와 Spring Container를 이해하는 핵심 기반이 된다.

## 다음으로 연결되는 주제

역할과 구현을 분리하더라도 구현을 재사용하는 방식은 신중하게 선택해야 한다.

다음 주제는 상속보다 조합이다. 상속으로 기능을 물려받는 방식과 조합으로 필요한 객체를 사용하는 방식의 차이를 살펴본다.
