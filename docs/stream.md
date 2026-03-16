# Java Stream

Java Stream은 컬렉션 데이터를 **선언적인 방식으로 처리하기 위한 API**이다.
데이터를 저장하는 구조가 아니라 **데이터 처리 흐름을 표현하는 도구**이다.

Stream을 사용하면 데이터를 다음과 같은 방식으로 처리할 수 있다.

* 필터링
* 변환
* 정렬
* 집계
* 그룹화

---

<!-- ===================================== -->

<!-- STUDY NOTE -->

<!-- 이 문서는 Java Stream 학습 기록용 문서 -->

<!-- 예제 코드는 examples/stream 폴더에 위치 -->

<!-- ===================================== -->

# 1. Stream 개념

Stream은 컬렉션 데이터를 **순차적으로 처리하는 파이프라인**이다.

Stream은 다음 특징을 가진다.

* 데이터를 저장하지 않는다
* 원본 데이터를 변경하지 않는다
* 내부 반복을 사용한다
* 중간 연산과 최종 연산으로 구성된다

예시

```java
List<String> names = List.of("alice", "bob", "andrew");

List<String> result = names.stream()
        .filter(name -> name.startsWith("a"))
        .map(String::toUpperCase)
        .toList();
```

---

# 2. 반복문 vs Stream

기존 반복문 방식

```java
List<String> result = new ArrayList<>();

for (String name : names) {
    if (name.startsWith("a")) {
        result.add(name.toUpperCase());
    }
}
```

Stream 방식

```java
List<String> result = names.stream()
        .filter(name -> name.startsWith("a"))
        .map(String::toUpperCase)
        .toList();
```

Stream은 **무엇을 하는 코드인지 명확하게 표현된다.**

---

# 3. Stream Pipeline

Stream은 **Pipeline 구조**로 동작한다.

Pipeline은 다음 3단계로 구성된다.

```
Data Source -> Intermediate Operation -> Terminal Operation
```

예시

```java
names.stream()
     .filter(name -> name.startsWith("a"))
     .map(String::toUpperCase)
     .toList();
```

구조

```
Data Source
   ↓
stream()

Intermediate Operation
   ↓
filter()
map()

Terminal Operation
   ↓
toList()
```

---

# 4. Intermediate Operation

중간 연산은 데이터를 변환하거나 필터링하는 연산이다.

대표적인 연산

| 연산       | 설명            |
| -------- | ------------- |
| filter   | 조건에 맞는 데이터 추출 |
| map      | 데이터 변환        |
| sorted   | 정렬            |
| distinct | 중복 제거         |
| limit    | 데이터 개수 제한     |
| skip     | 앞부분 데이터 건너뜀   |
| flatMap  | Stream을 평탄화   |

예제 코드

```
examples/stream/StreamFilterExample.java
examples/stream/StreamMapExample.java
examples/stream/StreamFlatMapExample.java
```

<!-- TODO
flatMap 예제 추가 예정
-->

---

# 5. Terminal Operation

최종 연산은 Stream을 소비하여 결과를 생성한다.

대표적인 연산

| 연산        | 설명       |
| --------- | -------- |
| forEach   | 요소 반복    |
| collect   | 컬렉션 변환   |
| count     | 요소 개수 계산 |
| findFirst | 첫 요소 반환  |
| anyMatch  | 조건 검사    |
| reduce    | 누적 연산    |

예시

```java
long count = names.stream()
        .filter(name -> name.startsWith("a"))
        .count();
```

---

# 6. Lazy Evaluation

Stream은 **Lazy Evaluation(지연 연산)** 방식으로 동작한다.

즉, **최종 연산이 실행되기 전까지 실제 연산이 수행되지 않는다.**

예시

```java
names.stream()
        .filter(name -> {
            System.out.println("filter 실행");
            return name.startsWith("a");
        })
        .map(String::toUpperCase);
```

이 코드는 **아무것도 출력되지 않는다.**

왜냐하면 **Terminal Operation이 없기 때문이다.**

다음 코드에서 실행된다.

```java
names.stream()
        .filter(name -> {
            System.out.println("filter 실행");
            return name.startsWith("a");
        })
        .map(String::toUpperCase)
        .toList();
```

---

# 7. 객체 리스트 처리

Stream은 객체 리스트 처리에서 가장 많이 사용된다.

예시 클래스

```java
public class Person {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

}
```

사용 예시

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

예제 코드

```
examples/stream/StreamObjectExample.java
```

---

# 8. Stream 실무 패턴

Stream은 실무에서 다음 패턴으로 많이 사용된다.

### 1. 필터링

```
filter()
```

### 2. 데이터 변환

```
map()
```

### 3. 그룹화

```
groupingBy()
```

### 4. 개수 세기

```
count()
Collectors.counting()
```

### 5. 정렬

```
sorted()
```

예시

```java
Map<String, Long> apiKeyCounts =
        logs.stream()
            .map(LogEntry::getApiKey)
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(
                key -> key,
                Collectors.counting()
            ));
```

이 패턴은 **로그 분석 / 통계 / 데이터 집계**에서 자주 사용된다.

---

# 9. Parallel Stream

Stream은 병렬 처리도 지원한다.

```java
list.parallelStream()
```

예시

```java
list.parallelStream()
        .map(String::toUpperCase)
        .toList();
```

하지만 다음 문제점이 있다.

* Thread 관리 비용
* 순서 보장 문제
* 작은 작업에서는 성능 저하

따라서 **대량 데이터 처리에서만 사용해야 한다.**

<!-- TODO
parallel stream 상세 설명 추가 예정
-->

---

# 10. Stream 사용 시 주의점

### Stream은 재사용할 수 없다

```java
Stream<String> stream = list.stream();

stream.forEach(System.out::println);

// stream.forEach(System.out::println); // 예외 발생
```

Stream은 **한 번 소비되면 다시 사용할 수 없다.**

---

### 복잡한 Stream은 가독성이 떨어질 수 있다

예

```java
data.stream()
    .filter(...)
    .map(...)
    .sorted(...)
    .limit(...)
```

너무 긴 체이닝은 **가독성을 떨어뜨릴 수 있다.**

---

### 간단한 반복은 for문이 더 좋을 수 있다

Stream은 **모든 상황에 최적의 도구는 아니다.**

---

# 11. 성능 고려사항

Stream 사용 시 고려해야 할 점

* 불필요한 객체 생성
* Boxing / Unboxing
* 너무 많은 중간 연산
* parallelStream 오남용

성능이 중요한 경우

* primitive stream 사용

예

```java
IntStream
LongStream
DoubleStream
```

---

# 12. Example Code

Stream 관련 예제 코드는 다음 위치에서 확인할 수 있다.

```
examples/stream/
```

예제 목록

```
[StreamFilterExample](../examples/stream/StreamFilterExample.java)
[StreamMapExample](../examples/stream/StreamMapExample.java)
[StreamFilterMapExample](../examples/stream/StreamFilterMapExample.java)
[StreamCollectExample](../examples/stream/StreamCollectExample.java)
[StreamGroupingExample](../examples/stream/StreamGroupingExample.java)
[StreamObjectExample](../examples/stream/StreamObjectExample.java)
```

GitHub 링크

<!-- TODO
GitHub 코드 링크 추가 예정
-->

---
<!--
# 13. 추가 학습 예정
-->
<!-- ===================================== -->

<!-- TODO -->

<!-- ===================================== -->
<!--
추가로 정리할 내용

* flatMap
* reduce
* Optional + Stream
* Stream Debugging
* Stream 성능 비교
* 실무 Stream 패턴
* 로그 분석 프로젝트 Stream 활용

---
-->

# Summary

Java Stream은 컬렉션 데이터를 **선언적으로 처리하기 위한 API**이다.

특히 다음 작업에서 강력하다.

* 데이터 필터링
* 데이터 변환
* 데이터 집계
* 객체 리스트 처리

Stream을 적절히 사용하면 **가독성이 좋은 코드**를 작성할 수 있다.
