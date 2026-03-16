# Java Stream

Java Stream은 컬렉션 데이터를 **선언적인 방식으로 처리하기 위한 API**이다.  
데이터를 저장하는 구조가 아니라 **데이터 처리 흐름을 표현하는 도구**이다.

Stream을 사용하면 컬렉션 데이터를 **필터링, 변환, 정렬, 집계**하는 작업을 간결하게 표현할 수 있다.

---

# 1. Stream이란

Java에서 컬렉션(List, Set 등)을 처리할 때 일반적으로 반복문을 사용한다.

기존 방식은 다음과 같다.

```java
List<String> names = List.of("alice", "bob", "andrew");
List<String> result = new ArrayList<>();

for (String name : names) {
    if (name.startsWith("a")) {
        result.add(name.toUpperCase());
    }
}
```
Stream을 사용하면 다음과 같이 표현할 수 있다.

List<String> names = List.of("alice", "bob", "andrew");

List<String> result = names.stream()
        .filter(name -> name.startsWith("a"))
        .map(String::toUpperCase)
        .toList();

이 방식은 데이터 처리 흐름을 단계적으로 표현할 수 있다는 장점이 있다.

---

# 2. Stream의 특징

Java Stream은 다음과 같은 특징을 가진다.

## 1. 원본 데이터를 변경하지 않는다

Stream은 데이터를 가공하여 새로운 결과를 생성한다.

## 2. 내부 반복을 사용한다

반복문을 직접 작성하지 않고 Stream API가 반복을 처리한다.

## 3. 중간 연산과 최종 연산으로 구성된다

Stream 연산은 크게 두 가지로 나뉜다.

**중간 연산 (Intermediate Operation)**
데이터를 변환하거나 필터링하는 단계

예
- filter
- map
- sorted
- distinct

**최종 연산 (Terminal Operation)**
Stream을 소비하여 결과를 생성하는 단계

예
- forEach
- collect
- count
- findFirst

---

# 3. Stream 생성

컬렉션에서 Stream을 생성할 수 있다.

```java
List<String> list = List.of("a", "b", "c");

Stream<String> stream = list.stream();
```

# 4. 주요 Stream 연산

Stream에서 자주 사용되는 연산들을 정리한다.

| 연산       | 설명             |
| -------- | -------------- |
| filter   | 조건에 맞는 데이터만 추출 |
| map      | 데이터를 다른 형태로 변환 |
| sorted   | 정렬             |
| distinct | 중복 제거          |
| limit    | 데이터 개수 제한      |
| collect  | 결과를 컬렉션으로 변환   |
| count    | 요소 개수 계산       |

---

# 5. filter

조건에 맞는 데이터만 추출할 때 사용한다.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

List<Integer> evenNumbers = numbers.stream()
        .filter(n -> n % 2 == 0)
        .toList();
```

<!--
예제 코드

examples/stream/StreamFilterExample.java

(추후 GitHub 링크 추가 예정)
-->

---

# 6. map

데이터를 다른 값으로 변환할 때 사용한다.

```java
List<String> names = List.of("java", "spring", "stream");

List<String> result = names.stream()
        .map(String::toUpperCase)
        .toList();
```

<!--
예제 코드

examples/stream/StreamMapExample.java
-->

---

# 7. filter + map

실무에서 가장 많이 사용하는 Stream 패턴이다.

```java
List<String> names = List.of("alice", "bob", "anna", "charlie");

List<String> result = names.stream()
        .filter(name -> name.startsWith("a"))
        .map(String::toUpperCase)
        .toList();
```

<!--
예제 코드

examples/stream/StreamFilterMapExample.java
-->

---

# 8. collect

Stream의 결과를 컬렉션으로 변환할 때 사용한다.

```java
List<String> list = List.of("a", "b", "c");

List<String> result = list.stream()
        .collect(Collectors.toList());
```

<!--
예제 코드

examples/stream/StreamCollectExample.java
-->

---

# 9. groupingBy

데이터를 그룹화할 때 사용한다.

```java
List<String> fruits = List.of("apple", "banana", "apple", "orange");

Map<String, Long> countMap = fruits.stream()
        .collect(Collectors.groupingBy(
                fruit -> fruit,
                Collectors.counting()
        ));
```

<!--
예제 코드

examples/stream/StreamGroupingExample.java
-->

---

# 10. 객체 리스트 처리 예제

Stream은 객체 리스트를 처리할 때 특히 유용하다.

```java
List<Person> people = List.of(
        new Person("Kim", 20),
        new Person("Lee", 17),
        new Person("Park", 25)
);

List<String> adultNames = people.stream()
        .filter(person -> person.getAge() >= 20)
        .map(Person::getName)
        .toList();
```

<!---
예제 코드

examples/stream/StreamObjectExample.java
-->

---

# 11. Stream 사용 시 주의점
Stream은 재사용할 수 없다

```java
Stream<String> stream = List.of("a", "b", "c").stream();

stream.forEach(System.out::println);

// stream.forEach(System.out::println); // 예외 발생
```

최종 연산이 실행되면 Stream은 소비되며 다시 사용할 수 없다.

---

**모든 경우에 Stream이 좋은 것은 아니다**

간단한 반복문은 오히려 for문이 더 가독성이 좋은 경우도 있다.

---

# 12. 정리

Java Stream은 컬렉션 데이터를 선언적으로 처리하기 위한 API이다.

특히 다음과 같은 작업에서 강력하다.

- 데이터 필터링
- 데이터 변환
- 데이터 집계
- 객체 리스트 처리

Stream을 적절히 사용하면 가독성과 유지보수성이 좋은 코드를 작성할 수 있다.