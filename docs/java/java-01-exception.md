# Java 예외 처리

## 왜 배우는가

예외 처리는 프로그램이 예상하지 못한 상황을 만났을 때 어떻게 반응할지 정하는 방식이다.

입력값이 잘못되거나, 파일이 없거나, 외부 시스템 호출이 실패하는 상황은 실제 개발에서 자주 발생한다. 이때 예외를 이해하지 못하면 오류를 숨기거나, 어디서 문제가 났는지 알기 어려운 코드를 만들기 쉽다.

Java 예외 처리를 이해하면 객체가 잘못된 상태로 동작하지 않도록 막을 수 있고, 이후 Spring에서 다루는 검증, 전역 예외 처리, API 에러 응답 설계와도 자연스럽게 이어진다.

## 개념

예외는 프로그램 실행 중 발생하는 비정상 상황을 표현하는 객체다.

Java에서는 문제가 발생하면 `throw`로 예외를 던지고, 필요한 곳에서 `try-catch`로 처리할 수 있다.

예외는 크게 두 종류로 나눌 수 있다.

- Checked Exception: 컴파일 시점에 처리 여부를 확인하는 예외
- Unchecked Exception: 실행 중 발생할 수 있는 예외로, 컴파일러가 강제하지 않는 예외

대표적으로 `IOException`은 checked exception이고, `IllegalArgumentException`, `IllegalStateException`, `NullPointerException`은 unchecked exception이다.

또한 `throws`는 메서드가 어떤 예외를 바깥으로 넘길 수 있는지 선언할 때 사용한다.

## 핵심

- 예외는 오류 상황을 코드로 표현하는 방식이다.
- `throw`는 예외를 발생시키고, `throws`는 예외를 호출한 쪽으로 넘길 수 있음을 선언한다.
- 복구 가능한 상황인지, 프로그래밍 실수인지에 따라 예외 종류와 처리 위치를 다르게 봐야 한다.
- 잘못된 입력이나 잘못된 상태는 조용히 넘어가기보다 예외로 빠르게 드러내는 것이 좋다.
- 모든 곳에서 `try-catch`로 잡기보다, 처리할 수 있는 위치에서만 잡는 것이 좋다.

## 코드 비교

다음 코드는 잘못된 금액이 들어와도 그냥 출금을 진행한다.

```java
class Account {
    private int balance = 10000;

    void withdraw(int amount) {
        balance -= amount;
    }
}
```

이 코드는 음수 금액이나 잔액보다 큰 금액이 들어와도 막지 못한다. 객체가 잘못된 상태가 될 수 있다.

예외를 사용하면 잘못된 입력과 상태를 빠르게 막을 수 있다.

```java
class Account {
    private int balance = 10000;

    void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
        }

        if (balance < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        balance -= amount;
    }
}
```

이제 `Account`는 잘못된 요청을 조용히 받아들이지 않는다. 자신의 상태를 지키기 위해 예외를 던진다.

checked exception과 `throws`는 다음처럼 볼 수 있다.

```java
import java.io.IOException;

class FileLoader {
    String load() throws IOException {
        throw new IOException("파일을 찾을 수 없습니다.");
    }
}
```

이 경우 `load()`를 호출하는 쪽은 예외를 처리하거나 다시 던져야 한다.

## 동작 흐름

1. 메서드 실행 중 문제가 발생한다.
2. 코드가 `throw`로 예외 객체를 던진다.
3. 현재 메서드는 즉시 종료되고, 호출한 쪽으로 예외가 전달된다.
4. 호출 스택을 따라 올라가며 처리할 `catch`를 찾는다.
5. 처리할 곳이 없으면 프로그램은 예외와 함께 종료되거나, 상위 프레임워크가 공통 처리한다.

Spring MVC에서는 이 흐름이 이어져서 Controller에서 발생한 예외를 `@ExceptionHandler`나 `@ControllerAdvice`가 받아 공통 응답으로 바꿀 수 있다.

## 주의할 점

예외를 사용한다고 해서 모든 문제를 `try-catch`로 감싸는 것은 좋지 않다.

예외를 잡았으면 복구, 변환, 기록 중 하나는 해야 한다. 단순히 예외를 무시하면 문제 원인을 찾기 어려워진다.

또한 비즈니스 규칙 위반과 시스템 장애를 같은 예외로 다루면 코드 의미가 흐려질 수 있다. 예를 들어 잘못된 사용자 입력은 `IllegalArgumentException`처럼 표현할 수 있고, 외부 시스템 연결 실패는 다른 예외로 구분하는 편이 좋다.

checked exception을 남용하면 메서드 선언과 호출부가 너무 복잡해질 수 있다. 학습 초기에는 다음 기준으로 이해하면 좋다.

- 잘못된 입력, 잘못된 상태: 보통 unchecked exception
- 파일, 네트워크, DB 같은 외부 자원 문제: 상황에 따라 checked 또는 프레임워크 예외

## 정리

예외 처리는 프로그램의 비정상 상황을 드러내고, 잘못된 상태로 계속 실행되지 않게 막는 장치다.

중요한 것은 예외 문법 자체보다, 어떤 문제를 어디서 감지하고 어디서 처리할지 판단하는 것이다. Java에서 이 기준을 잡아두면 이후 Spring의 검증과 전역 예외 처리도 더 쉽게 이해할 수 있다.

## 다음으로 연결되는 주제

Java 예외 처리를 이해했다면 다음은 Spring에서 예외를 HTTP 응답으로 어떻게 바꾸는지 연결해보면 좋다.

다음 주제는 [MVC](../spring/04-mvc.md)다. MVC에서는 요청 처리 흐름과 함께 검증, 예외 처리, 공통 에러 응답 구성을 다룬다.
