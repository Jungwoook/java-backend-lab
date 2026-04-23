# 좋은 객체 설계와 나쁜 객체 설계 비교

## 왜 배우는가

객체지향 개념을 따로 배우는 것만으로는 실제 코드에서 좋은 설계를 판단하기 어렵다.

책임, 캡슐화, 응집도, 결합도, 다형성, 조합, SOLID는 결국 하나의 질문으로 모인다.

"요구사항이 바뀌었을 때 이 코드는 어디까지 흔들리는가?"

좋은 설계와 나쁜 설계를 비교하면 객체지향 원칙이 왜 필요한지 더 분명해진다.

## 개념

나쁜 객체 설계는 보통 다음 특징을 가진다.

- 한 클래스가 너무 많은 일을 한다.
- 객체의 내부 상태를 외부에서 직접 변경한다.
- 구체 구현에 강하게 의존한다.
- 조건문이 늘어나면서 변경 범위가 커진다.
- 비즈니스 규칙이 여러 곳에 흩어진다.

좋은 객체 설계는 반대로 책임이 분명하고, 변경 가능성이 높은 부분을 역할로 분리한다.

## 핵심

- 좋은 설계는 변경 위치를 예측하기 쉽다.
- 좋은 객체는 자신의 상태와 규칙을 스스로 관리한다.
- 좋은 객체는 다른 객체의 내부 구현을 과하게 알지 않는다.
- 변경 가능성이 높은 부분은 역할과 구현으로 분리한다.
- 설계는 처음부터 완벽하게 만드는 것이 아니라 변경을 보며 개선한다.

## 코드 비교

다음 코드는 주문, 할인, 결제 책임이 한곳에 모여 있다.

```java
class OrderService {
    void order(String grade, int price, String paymentType) {
        int discountPrice = 0;

        if (grade.equals("VIP")) {
            discountPrice = 1000;
        }

        int finalPrice = price - discountPrice;

        if (paymentType.equals("CARD")) {
            System.out.println("카드 결제: " + finalPrice);
        } else if (paymentType.equals("BANK")) {
            System.out.println("계좌 이체 결제: " + finalPrice);
        }
    }
}
```

회원 등급 할인 정책이 바뀌어도, 결제 방식이 추가되어도 `OrderService`를 수정해야 한다. 변경 이유가 여러 개이고 조건문이 계속 늘어난다.

아래처럼 역할을 나눌 수 있다.

```java
interface DiscountPolicy {
    int discount(String grade, int price);
}

class GradeDiscountPolicy implements DiscountPolicy {
    public int discount(String grade, int price) {
        if (grade.equals("VIP")) {
            return 1000;
        }
        return 0;
    }
}

interface Payment {
    void pay(int amount);
}

class CardPayment implements Payment {
    public void pay(int amount) {
        System.out.println("카드 결제: " + amount);
    }
}

class OrderService {
    private final DiscountPolicy discountPolicy;
    private final Payment payment;

    OrderService(DiscountPolicy discountPolicy, Payment payment) {
        this.discountPolicy = discountPolicy;
        this.payment = payment;
    }

    void order(String grade, int price) {
        int discountPrice = discountPolicy.discount(grade, price);
        int finalPrice = price - discountPrice;

        payment.pay(finalPrice);
    }
}
```

이제 `OrderService`는 주문 흐름을 담당하고, 할인과 결제는 각각의 역할이 담당한다. 새로운 결제 방식은 `Payment` 구현체로 추가할 수 있다.

## 동작 흐름

1. `OrderService`가 주문 흐름을 시작한다.
2. 할인 계산은 `DiscountPolicy`에게 맡긴다.
3. 결제 처리는 `Payment`에게 맡긴다.
4. `OrderService`는 전체 흐름만 조율한다.

책임이 분리되면 변경도 해당 책임을 가진 객체 주변에서 일어난다.

## 주의할 점

좋은 설계는 무조건 많은 클래스와 인터페이스를 만드는 것이 아니다.

작은 프로그램에서는 단순한 코드가 더 좋을 수 있다. 하지만 변경 가능성이 보이기 시작하면 책임을 나누고, 역할을 분리하고, 구현을 교체할 수 있는 구조로 개선해야 한다.

설계는 정답을 맞히는 일이 아니라 트레이드오프를 선택하는 일이다.

## 정리

나쁜 객체 설계는 변경 이유가 한곳에 섞여 있고, 구체 구현에 강하게 묶여 있다.

좋은 객체 설계는 책임이 분명하고, 객체가 자신의 상태와 규칙을 관리하며, 변경 가능성이 높은 부분을 역할로 분리한다. 지금까지 배운 객체지향 개념은 모두 변경에 강한 코드를 만들기 위한 도구다.

## 다음으로 연결되는 주제

객체지향 설계를 이해했다면 다음 단계는 객체들이 어떻게 생성되고 연결되는지 살펴보는 것이다.

다음 주제는 [DI / IoC](../spring/01-di-ioc.md)다. DI / IoC는 객체 사이의 의존 관계를 직접 만들지 않고 외부에서 연결하는 방식이다.
