# 캡슐화

## 왜 배우는가

캡슐화는 객체가 자신의 상태를 스스로 지키도록 만드는 설계 원칙이다.

객체의 내부 값이 외부 코드에 그대로 노출되면, 그 값을 사용하는 코드가 여기저기 퍼진다. 그러면 상태 변경 규칙이 바뀔 때 객체 하나만 고치는 것이 아니라 외부 코드까지 함께 고쳐야 한다.

Spring이나 JPA를 사용할 때도 캡슐화는 중요하다. Entity의 상태를 아무 곳에서나 바꾸게 두면 비즈니스 규칙이 흩어지고, 서비스 코드가 점점 복잡해진다.

## 개념

캡슐화는 객체의 상태와 그 상태를 다루는 행동을 하나로 묶고, 외부에는 필요한 기능만 공개하는 것이다.

캡슐화의 핵심은 단순히 필드를 `private`으로 만드는 것이 아니다. 중요한 것은 객체 내부의 상태 변경 규칙을 객체 자신이 관리하도록 만드는 것이다.

예를 들어 계좌 잔액은 아무 값으로나 바뀌면 안 된다. 출금 금액이 잔액보다 크면 출금할 수 없어야 한다. 이 규칙은 계좌를 사용하는 외부 코드가 아니라 `Account` 객체 안에 있어야 한다.

## 핵심

- 캡슐화는 데이터를 숨기는 것에서 끝나지 않는다.
- 객체가 자신의 상태 변경 규칙을 직접 관리해야 한다.
- 외부 코드는 객체의 내부 값보다 객체가 제공하는 행동에 의존해야 한다.
- getter와 setter를 무분별하게 만들면 캡슐화가 깨질 수 있다.
- 캡슐화가 잘 되면 변경이 객체 내부에 머무른다.

## 코드 비교

다음 코드는 잔액을 외부에서 직접 변경할 수 있는 예다.

```java
class Account {
    int balance;
}

class AccountService {
    void withdraw(Account account, int amount) {
        if (account.balance >= amount) {
            account.balance -= amount;
        }
    }
}
```

이 코드는 `AccountService`가 `Account`의 내부 상태를 직접 알고 있다. 다른 곳에서도 `balance`를 직접 수정할 수 있으므로 계좌의 규칙을 지키기 어렵다.

아래처럼 출금 책임을 `Account` 안으로 옮길 수 있다.

```java
class Account {
    private int balance;

    Account(int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("잔액은 0보다 작을 수 없습니다.");
        }
        this.balance = balance;
    }

    void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
        }

        if (balance < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        balance -= amount;
    }

    int getBalance() {
        return balance;
    }
}
```

이제 잔액 변경 규칙은 `Account` 내부에 모인다. 외부 코드는 `balance`를 직접 바꾸지 않고 `withdraw()`라는 행동을 요청한다.

## 동작 흐름

1. 외부 코드는 `Account`에게 출금을 요청한다.
2. `Account`는 출금 금액이 유효한지 확인한다.
3. `Account`는 잔액이 충분한지 확인한다.
4. 모든 조건을 만족하면 `balance`를 변경한다.

상태를 가진 객체가 자신의 상태 변경을 직접 처리하므로, 규칙이 한곳에 모인다.

## 주의할 점

모든 필드에 getter와 setter를 자동으로 만드는 습관은 조심해야 한다.

```java
account.setBalance(-1000);
```

이런 코드가 가능하다면 객체가 자신의 상태를 지키지 못한다는 뜻이다. setter가 필요할 때도 있지만, 먼저 "이 값을 직접 바꾸게 해도 되는가?"를 생각해야 한다.

또한 getter도 무조건 안전하지 않다. 외부에서 값을 꺼내 판단하는 코드가 많아지면 객체의 책임이 외부로 새어 나갈 수 있다.

## 정리

캡슐화는 객체의 상태를 숨기는 기술이 아니라, 객체가 자신의 규칙을 지키도록 만드는 설계 방식이다.

좋은 캡슐화는 외부 코드가 객체의 내부 구조보다 객체의 행동에 의존하게 만든다. 덕분에 내부 구현이 바뀌어도 외부 코드의 변경을 줄일 수 있다.

## 다음으로 연결되는 주제

캡슐화를 통해 객체 안에 책임을 모았다면, 이제 객체와 객체 사이의 관계를 살펴봐야 한다.

다음 주제는 응집도와 결합도다. 응집도와 결합도는 책임이 한 객체 안에 잘 모였는지, 객체들이 서로 지나치게 얽혀 있지는 않은지 판단하는 기준이다.
