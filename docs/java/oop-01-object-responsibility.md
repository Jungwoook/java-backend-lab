# 객체와 책임

## 왜 배우는가

객체지향 프로그래밍은 클래스를 많이 만드는 기술이 아니라, 프로그램을 여러 객체의 협력으로 나누어 변경하기 쉽게 만드는 방식이다.

객체를 잘 나누려면 먼저 각 객체가 어떤 책임을 가져야 하는지 정해야 한다. 책임이 분명하지 않으면 한 클래스가 너무 많은 일을 하게 되고, 작은 요구사항 변경에도 여러 코드가 함께 흔들린다.

Spring을 배울 때도 이 개념은 계속 등장한다. Controller, Service, Repository를 나누는 이유도 각 계층과 객체의 책임을 분리하기 위해서다.

## 개념

객체는 상태와 행동을 함께 가진다.

- 상태: 객체가 알고 있는 값
- 행동: 객체가 수행할 수 있는 일
- 책임: 객체가 맡아서 해야 하는 역할이나 판단

객체지향 설계에서 중요한 질문은 "이 데이터를 어디에 둘까?"보다 "이 일을 누가 책임져야 할까?"이다.

예를 들어 주문 금액을 계산하는 프로그램이 있다고 하자. 주문 금액 계산은 단순히 숫자를 더하는 일이 아니라, 주문에 포함된 상품들과 수량을 알고 있는 객체가 맡는 것이 자연스럽다. 즉, 주문과 관련된 판단은 `Order` 객체의 책임이 될 수 있다.

## 핵심

- 객체는 데이터를 담는 상자가 아니라, 책임을 가진 협력 단위다.
- 책임은 객체가 알아야 하는 것과 해야 하는 것을 기준으로 정한다.
- 객체가 가진 데이터와 관련된 행동은 가능하면 그 객체 안에 둔다.
- 한 객체가 너무 많은 책임을 가지면 변경에 약해진다.
- 책임을 잘 나누면 코드의 변경 범위가 줄어든다.

좋은 책임 분배는 "변경이 생겼을 때 어디를 고치면 되는가?"라는 질문에 명확하게 답할 수 있게 해준다.

## 코드 비교

다음 코드는 주문 금액 계산 책임이 외부 코드에 흩어져 있는 예다.

```java
class OrderItem {
    int price;
    int quantity;

    OrderItem(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }
}

class OrderService {
    int calculateTotalPrice(List<OrderItem> items) {
        int totalPrice = 0;

        for (OrderItem item : items) {
            totalPrice += item.price * item.quantity;
        }

        return totalPrice;
    }
}
```

이 코드에서 `OrderService`는 주문 항목의 가격과 수량을 직접 꺼내 계산한다. 지금은 단순하지만, 할인이나 세금 같은 규칙이 추가되면 `OrderService`가 점점 많은 계산 규칙을 알게 된다.

아래처럼 각 객체가 자기 책임을 가지도록 바꿀 수 있다.

```java
class OrderItem {
    private final int price;
    private final int quantity;

    OrderItem(int price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    int calculatePrice() {
        return price * quantity;
    }
}

class Order {
    private final List<OrderItem> items;

    Order(List<OrderItem> items) {
        this.items = items;
    }

    int calculateTotalPrice() {
        int totalPrice = 0;

        for (OrderItem item : items) {
            totalPrice += item.calculatePrice();
        }

        return totalPrice;
    }
}
```

이제 `OrderItem`은 자기 금액을 계산할 책임을 가진다. `Order`는 주문 항목들을 모아 전체 주문 금액을 계산할 책임을 가진다.

계산 규칙이 바뀌어도 변경 위치가 더 명확해진다.

## 동작 흐름

위 개선 코드의 흐름은 다음과 같다.

1. `Order`가 전체 주문 금액 계산을 시작한다.
2. `Order`는 각 `OrderItem`에게 자신의 금액을 계산하라고 요청한다.
3. `OrderItem`은 자신의 `price`와 `quantity`를 이용해 금액을 계산한다.
4. `Order`는 각 주문 항목의 금액을 더해 전체 금액을 반환한다.

중요한 점은 `Order`가 `OrderItem`의 세부 계산 방식을 직접 알지 않는다는 것이다. `Order`는 각 항목에게 메시지를 보내고, 각 항목은 자신의 책임을 수행한다.

## 주의할 점

객체에 책임을 나눈다고 해서 무조건 클래스를 많이 만들어야 하는 것은 아니다. 책임이 거의 없거나 변경 가능성이 낮은 코드를 억지로 분리하면 오히려 읽기 어려워질 수 있다.

또한 책임을 나눌 때는 단순히 메서드를 다른 클래스로 옮기는 것에 그치면 안 된다. 객체가 자신의 상태를 스스로 판단하고 행동하도록 만들어야 한다.

다음과 같은 코드는 주의해야 한다.

```java
if (orderItem.getPrice() * orderItem.getQuantity() > 10000) {
    // ...
}
```

외부에서 객체의 값을 꺼내 판단하고 있다면, 그 판단이 객체 내부의 책임이 될 수 있는지 먼저 생각해보는 것이 좋다.

## 정리

객체지향 설계의 출발점은 객체와 책임이다.

객체는 단순히 데이터를 저장하는 구조가 아니라, 자신이 맡은 일을 수행하는 단위다. 좋은 객체는 자신의 상태와 관련된 행동을 스스로 처리한다.

책임을 적절히 나누면 코드가 더 읽기 쉬워지고, 변경이 생겼을 때 수정해야 할 위치도 명확해진다.

## 다음으로 연결되는 주제

객체가 자신의 책임을 제대로 수행하려면 내부 상태를 외부에 함부로 노출하지 않아야 한다.

다음 주제는 캡슐화다. 캡슐화는 객체가 자신의 상태와 책임을 지키기 위한 중요한 설계 원칙이다.
