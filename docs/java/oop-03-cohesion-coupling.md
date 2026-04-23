# 응집도와 결합도

## 왜 배우는가

객체를 나누는 것만으로 좋은 설계가 되지는 않는다.

어떤 객체는 너무 많은 책임을 가지고 있고, 어떤 객체는 다른 객체의 내부 사정을 너무 많이 알고 있다. 이런 코드는 처음에는 동작해도 요구사항이 바뀌면 수정 범위가 커진다.

응집도와 결합도는 객체지향 설계가 좋은 방향으로 가고 있는지 확인하는 기준이다.

## 개념

응집도는 하나의 객체나 모듈 안에 관련 있는 책임이 얼마나 잘 모여 있는지를 나타낸다.

결합도는 하나의 객체가 다른 객체에 얼마나 많이 의존하는지를 나타낸다.

좋은 설계는 보통 높은 응집도와 낮은 결합도를 가진다.

- 높은 응집도: 관련 있는 책임이 한곳에 모여 있다.
- 낮은 응집도: 서로 관련 없는 책임이 한곳에 섞여 있다.
- 높은 결합도: 한 객체가 다른 객체의 구체적인 구현에 강하게 의존한다.
- 낮은 결합도: 객체들이 필요한 약속에만 의존한다.

## 핵심

- 응집도는 객체 내부의 책임이 얼마나 잘 모였는지를 보는 기준이다.
- 결합도는 객체 사이의 의존 관계가 얼마나 강한지를 보는 기준이다.
- 변경이 자주 함께 일어나는 코드는 같은 객체 안에 모이는 것이 좋다.
- 변경 이유가 다른 코드는 분리하는 것이 좋다.
- 구체 클래스보다 인터페이스에 의존하면 결합도를 낮출 수 있다.

## 코드 비교

다음 코드는 주문 처리, 결제, 알림 책임이 한 클래스에 모여 있다.

```java
class OrderService {
    void order(String productName, int price, String email) {
        System.out.println(productName + " 주문 생성");
        System.out.println(price + "원 결제");
        System.out.println(email + "로 주문 완료 메일 발송");
    }
}
```

이 클래스는 주문 정책이 바뀌어도, 결제 방식이 바뀌어도, 메일 발송 방식이 바뀌어도 수정될 수 있다. 변경 이유가 여러 개이므로 응집도가 낮다.

아래처럼 책임을 나눌 수 있다.

```java
class OrderService {
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    OrderService(PaymentService paymentService, NotificationService notificationService) {
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    void order(String productName, int price, String email) {
        System.out.println(productName + " 주문 생성");
        paymentService.pay(price);
        notificationService.send(email);
    }
}

class PaymentService {
    void pay(int price) {
        System.out.println(price + "원 결제");
    }
}

class NotificationService {
    void send(String email) {
        System.out.println(email + "로 주문 완료 메일 발송");
    }
}
```

이제 주문 흐름은 `OrderService`, 결제는 `PaymentService`, 알림은 `NotificationService`가 맡는다. 각 클래스의 변경 이유가 더 분명해졌다.

## 동작 흐름

1. `OrderService`가 주문 흐름을 시작한다.
2. 결제가 필요하면 `PaymentService`에게 요청한다.
3. 알림이 필요하면 `NotificationService`에게 요청한다.
4. 각 객체는 자신이 맡은 책임만 수행한다.

`OrderService`는 전체 흐름을 조율하지만, 결제와 알림의 세부 구현을 직접 처리하지 않는다.

## 주의할 점

책임을 너무 잘게 나누는 것도 문제가 될 수 있다. 클래스가 많아졌는데 각 클래스가 하는 일이 거의 없다면 오히려 흐름을 파악하기 어려워진다.

응집도와 결합도는 절대적인 숫자로 판단하기보다 변경 이유를 기준으로 생각하는 것이 좋다.

"이 코드는 어떤 이유로 바뀔까?"라는 질문을 던졌을 때 여러 이유가 떠오르면 분리를 고민할 수 있다.

## 정리

응집도는 객체 내부를 보는 기준이고, 결합도는 객체 사이를 보는 기준이다.

좋은 객체지향 설계는 관련 있는 책임을 모으고, 불필요한 의존은 줄인다. 그렇게 하면 변경이 생겼을 때 영향 범위를 작게 만들 수 있다.

## 다음으로 연결되는 주제

결합도를 낮추려면 객체가 구체 구현이 아니라 역할에 의존하도록 만들어야 한다.

다음 주제는 다형성과 역할 분리다. 역할을 기준으로 의존하면 구현을 바꾸더라도 사용하는 코드를 덜 수정할 수 있다.
