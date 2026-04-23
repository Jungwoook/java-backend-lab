# 상속보다 조합

## 왜 배우는가

상속은 객체지향의 중요한 기능이지만, 잘못 사용하면 코드 변경을 어렵게 만든다.

부모 클래스의 구현에 자식 클래스가 강하게 묶이기 때문이다. 부모 클래스의 작은 변경이 자식 클래스에 영향을 줄 수 있고, 자식 클래스가 원하지 않는 기능까지 물려받을 수도 있다.

조합은 필요한 기능을 다른 객체에게 맡기는 방식이다. 상속보다 관계가 느슨하고, 실행 시점에 구현을 바꿔 끼우기도 쉽다.

## 개념

상속은 "is-a" 관계를 표현한다.

조합은 "has-a" 또는 "uses-a" 관계를 표현한다.

예를 들어 `DiscountOrder`가 `Order`를 상속하면 할인 주문은 주문의 한 종류라는 의미가 된다. 반면 `Order`가 `DiscountPolicy`를 가지고 있으면 주문은 할인 정책을 사용한다는 의미가 된다.

객체지향 설계에서는 단순한 코드 재사용을 위해 상속을 사용하는 것을 조심해야 한다. 재사용이 목적이라면 조합이 더 나은 선택인 경우가 많다.

## 핵심

- 상속은 부모와 자식의 결합도가 높다.
- 상속은 진짜 종류 관계일 때 사용하는 것이 좋다.
- 코드 재사용만을 위해 상속을 사용하면 변경에 약해질 수 있다.
- 조합은 필요한 기능을 가진 객체를 필드로 두고 위임하는 방식이다.
- 조합은 구현 교체와 테스트에 유리하다.

## 코드 비교

다음 코드는 할인 주문을 상속으로 표현한 예다.

```java
class Order {
    int calculatePrice(int price) {
        return price;
    }
}

class DiscountOrder extends Order {
    @Override
    int calculatePrice(int price) {
        return price - 1000;
    }
}
```

처음에는 단순하지만, 정액 할인, 정률 할인, 쿠폰 할인, 회원 등급 할인처럼 정책이 늘어나면 상속 구조가 복잡해질 수 있다.

조합을 사용하면 할인 정책을 별도 객체로 분리할 수 있다.

```java
interface DiscountPolicy {
    int discount(int price);
}

class FixedDiscountPolicy implements DiscountPolicy {
    public int discount(int price) {
        return 1000;
    }
}

class Order {
    private final DiscountPolicy discountPolicy;

    Order(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    int calculatePrice(int price) {
        return price - discountPolicy.discount(price);
    }
}
```

이제 `Order`는 할인 정책을 상속받지 않고 사용한다. 할인 정책이 바뀌어도 `Order`의 구조는 안정적으로 유지된다.

## 동작 흐름

1. `Order`는 생성될 때 사용할 `DiscountPolicy`를 전달받는다.
2. 가격 계산이 필요하면 `DiscountPolicy`에게 할인 금액 계산을 요청한다.
3. `Order`는 원래 가격에서 할인 금액을 빼 최종 가격을 계산한다.
4. 할인 정책을 바꾸고 싶으면 다른 `DiscountPolicy` 구현체를 전달하면 된다.

## 주의할 점

상속이 항상 나쁜 것은 아니다. Java의 추상 클래스처럼 공통 상태와 기본 동작을 제공해야 하는 경우 상속이 자연스러울 수 있다.

다만 "기능을 재사용하고 싶다"는 이유만으로 상속을 선택하는 것은 위험하다. 부모 클래스의 변경이 자식 클래스 전체에 영향을 주기 때문이다.

상속을 사용할 때는 다음 질문을 먼저 해보는 것이 좋다.

- 정말 종류 관계인가?
- 자식 클래스가 부모 클래스의 모든 동작을 자연스럽게 받아들일 수 있는가?
- 부모 클래스 변경이 자식 클래스에 영향을 주어도 괜찮은가?

## 정리

상속은 강한 관계를 만들고, 조합은 필요한 기능을 객체에게 위임한다.

변경 가능성이 높은 기능은 상속보다 조합으로 분리하는 것이 유연하다. 특히 정책, 전략, 외부 연동 방식처럼 바뀔 가능성이 있는 부분은 조합과 인터페이스를 함께 사용하면 좋다.

## 다음으로 연결되는 주제

객체지향 설계에는 지금까지 배운 책임, 캡슐화, 응집도, 결합도, 다형성, 조합을 정리한 대표 원칙들이 있다.

다음 주제는 SOLID 원칙이다.
