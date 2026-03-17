# Lombok

## 1. Lombok이란

Lombok은 Java에서 반복적으로 작성되는 Boilerplate Code(반복 코드) 를 자동으로 생성해주는 라이브러리이다.

Java에서는 단순한 데이터 객체를 만들 때도 다음과 같은 코드가 필요하다.

- getter
- setter
- constructor
- toString
- equals / hashCode

Lombok은 Annotation을 통해 이러한 코드를 컴파일 시점에 자동 생성한다.

주로 다음과 같은 클래스에서 많이 사용된다.

- DTO
- Entity
- VO
- Config 객체

특히 Spring Boot 프로젝트에서 거의 필수적으로 사용되는 라이브러리이다.

---

## 2. Lombok을 사용하는 이유

### Lombok을 사용하지 않는 경우

````java
public class User {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
````

### Lombok 사용

````java
@Getter
@Setter
public class User {

    private String name;
    private int age;

}
````

### 장점

- 코드 길이 감소
- 가독성 증가
- 반복 코드 제거

---

## 3. Lombok 동작 원리

Lombok은 컴파일 시점 Annotation Processor를 사용하여 코드를 생성한다.

### 동작 과정

1. Java Source Code
2. Lombok Annotation Processor
3. 코드 자동 생성
4. .class 파일 생성

즉

소스 코드에는 보이지 않지만 컴파일된 코드에는 메서드가 존재한다.

---

## 4. Lombok 설치

### Maven

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
```

### Gradle

```
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

IDE에서는 Lombok Plugin 설치가 필요할 수 있다.

---

## 5. 주요 Lombok Annotation

### @Getter / @Setter

getter와 setter 메서드를 생성한다.

````java
@Getter
@Setter
public class User {

    private String name;
    private int age;

}
````

### @ToString

객체 정보를 출력하는 toString() 메서드를 생성한다.

````java
@ToString
public class User {

    private String name;
    private int age;

}
````

출력 예

```
User(name=Tom, age=20)
```

### @EqualsAndHashCode

객체 비교에 사용되는 equals()와 hashCode()를 생성한다.

````java
@EqualsAndHashCode
public class User {

    private String name;
    private int age;

}
````

### @NoArgsConstructor

기본 생성자를 생성한다.

````java
@NoArgsConstructor
public class User {

    private String name;
    private int age;

}
````

주로 JPA Entity에서 사용된다.

### @AllArgsConstructor

모든 필드를 포함하는 생성자를 생성한다.

````java
@AllArgsConstructor
public class User {

    private String name;
    private int age;

}
````

### @RequiredArgsConstructor

final 또는 @NonNull 필드만 포함하는 생성자를 생성한다.

````java
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
````

Spring에서 의존성 주입에 가장 많이 사용되는 Lombok Annotation이다.

### @Data

여러 Lombok Annotation을 한 번에 포함한다.

포함 기능

- @Getter
- @Setter
- @ToString
- @EqualsAndHashCode
- @RequiredArgsConstructor

사용 예

````java
@Data
public class User {

    private String name;
    private int age;

}
````

### 주의

실무에서는 @Data 대신 필요한 Annotation만 사용하는 경우가 많다.

### @Builder

Builder Pattern을 쉽게 구현할 수 있게 한다.

````java
@Builder
public class User {

    private String name;
    private int age;

}
````

사용

````java
User user = User.builder()
        .name("Tom")
        .age(20)
        .build();
````

장점

- 가독성 증가
- 생성자 파라미터 순서 문제 방지

---

## 6. Lombok Logger

Lombok은 Logger 생성도 지원한다.

Spring Boot에서는 보통 @Slf4j를 사용한다.

````java
@Slf4j
@Service
public class LogService {

    public void test() {
        log.info("service started");
    }

}
````

Lombok이 다음 코드를 자동 생성한다.

````java
private static final Logger log =
    LoggerFactory.getLogger(LogService.class);
````

---

## 7. Lombok @Value (Immutable 객체)

@Value는 Immutable 객체를 생성한다.

````java
@Value
public class User {

    String name;
    int age;

}
````

특징

- class final
- field final
- setter 없음
- getter 생성
- constructor 생성

---

## 8. Spring에서 Lombok 사용 패턴

### Service

````java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
````

### DTO

````java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String name;
    private int age;

}
````

---

## 9. Lombok 사용 시 주의사항

1. IDE Plugin 필요

   IDE에서 Lombok Plugin이 필요할 수 있다.

2. @Data 남용 주의

   @Data는 모든 필드 setter를 생성하기 때문에 캡슐화가 약해질 수 있다.

3. 디버깅 어려움

   코드가 자동 생성되기 때문에 실제 코드 확인이 어렵다.

---

## 10. 정리

Lombok은 Java에서 반복적으로 작성되는 코드를 줄여주는 라이브러리이다.

### 장점

- 코드량 감소
- 가독성 증가
- 개발 속도 향상

### 단점

- 코드 생성이 보이지 않음
- IDE 의존성

Spring Boot 프로젝트에서는 DTO, Entity, Service 등에서 널리 사용된다.