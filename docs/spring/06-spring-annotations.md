# Spring Annotation 요약

## 왜 배우는가

Spring에서는 애노테이션을 자주 사용한다.

하지만 애노테이션을 단순히 외우려고 하면 금방 헷갈린다. 중요한 것은 이 애노테이션을 Spring이 보고 어떤 일을 하는지 이해하는 것이다.

이 문서는 자주 사용하는 Spring 애노테이션을 빠르게 확인하기 위한 요약 문서다.

## 개념

애노테이션은 코드에 붙이는 메타데이터다.

Spring은 클래스, 메서드, 필드, 파라미터에 붙은 애노테이션을 읽고 객체 등록, 의존성 주입, 요청 매핑, 트랜잭션 처리, JPA 매핑 같은 작업을 수행한다.

예를 들어 `@Service`가 붙은 클래스는 컴포넌트 스캔 대상이 되고, Spring Bean으로 등록될 수 있다.

```java
@Service
class MemberService {
}
```

Spring은 이 클래스를 보고 "애플리케이션에서 관리할 객체"라고 판단한다.

## 핵심

- 애노테이션은 Spring에게 힌트를 주는 메타데이터다.
- 애노테이션 자체가 모든 일을 하는 것이 아니라, Spring이 애노테이션을 읽고 동작한다.
- 같은 Bean 등록 애노테이션이라도 의미를 구분해서 쓰는 것이 좋다.
- MVC 애노테이션은 HTTP 요청과 Java 메서드를 연결한다.
- JPA 애노테이션은 Java 객체와 DB 테이블을 연결한다.
- `@Transactional`은 트랜잭션 경계를 만든다.

## Spring Core 관련 애노테이션

### `@Component`

클래스를 Spring Bean으로 등록할 수 있게 표시한다.

```java
@Component
class CardPayment {
    void pay(int amount) {
        System.out.println("카드 결제: " + amount);
    }
}
```

컴포넌트 스캔 대상 패키지 안에 있으면 Spring이 이 클래스를 찾아 Bean으로 등록한다.

### `@Controller`, `@Service`, `@Repository`

세 애노테이션은 모두 `@Component`를 포함한다. 즉, Bean 등록 대상이다.

차이는 역할을 드러내는 데 있다.

```java
@Controller
class MemberController {
}

@Service
class MemberService {
}

@Repository
class MemberRepository {
}
```

- `@Controller`: HTTP 요청을 받는 계층
- `@Service`: 비즈니스 흐름을 처리하는 계층
- `@Repository`: 데이터 접근 계층

`@Repository`는 데이터 접근 예외를 Spring 예외로 변환하는 기능과도 연결된다.

### `@Configuration`

Spring 설정 클래스임을 나타낸다.

```java
@Configuration
class AppConfig {

    @Bean
    Payment payment() {
        return new CardPayment();
    }
}
```

설정 클래스 안에서 `@Bean` 메서드를 사용해 Bean을 직접 등록할 수 있다.

### `@Bean`

메서드가 반환하는 객체를 Spring Bean으로 등록한다.

```java
@Bean
OrderService orderService() {
    return new OrderService(payment());
}
```

외부 라이브러리 객체처럼 직접 클래스에 `@Component`를 붙일 수 없는 경우 자주 사용한다.

### `@Autowired`

Spring Bean을 주입받을 때 사용한다.

```java
@Service
class OrderService {
    private final Payment payment;

    @Autowired
    OrderService(Payment payment) {
        this.payment = payment;
    }
}
```

생성자가 하나만 있으면 `@Autowired`를 생략할 수 있다.

```java
@Service
class OrderService {
    private final Payment payment;

    OrderService(Payment payment) {
        this.payment = payment;
    }
}
```

학습 초기에는 생성자 주입을 기본으로 생각하는 것이 좋다.

### `@Primary`, `@Qualifier`

같은 타입의 Bean이 여러 개 있을 때 어떤 Bean을 주입할지 정한다.

```java
interface Payment {
    void pay(int amount);
}

@Primary
@Component
class CardPayment implements Payment {
    public void pay(int amount) {
        System.out.println("카드 결제");
    }
}

@Component
class BankPayment implements Payment {
    public void pay(int amount) {
        System.out.println("계좌 이체");
    }
}
```

`@Primary`가 붙은 Bean이 우선 선택된다.

특정 Bean을 이름으로 지정하려면 `@Qualifier`를 사용할 수 있다.

```java
@Service
class OrderService {
    private final Payment payment;

    OrderService(@Qualifier("bankPayment") Payment payment) {
        this.payment = payment;
    }
}
```

## Spring Boot 관련 애노테이션

### `@SpringBootApplication`

Spring Boot 애플리케이션의 시작 클래스에 붙인다.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

대표적으로 다음 기능을 포함한다.

- Spring 설정 클래스
- 자동 설정 활성화
- 컴포넌트 스캔

### `@ConfigurationProperties`

설정 파일의 값을 객체로 묶어서 받을 때 사용한다.

```yaml
payment:
  api-key: test-key
  timeout-seconds: 3
```

```java
@ConfigurationProperties(prefix = "payment")
public class PaymentProperties {
    private String apiKey;
    private int timeoutSeconds;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
```

설정값이 많을 때 `@Value`를 여러 번 쓰는 것보다 관리하기 좋다.

## Spring MVC 관련 애노테이션

### `@RestController`

REST API Controller를 만들 때 사용한다.

```java
@RestController
class MemberController {

    @GetMapping("/members")
    List<MemberResponse> findMembers() {
        return List.of(new MemberResponse("kim"));
    }
}
```

메서드 반환값을 View 이름이 아니라 HTTP response body로 변환한다.

### `@RequestMapping`, `@GetMapping`, `@PostMapping`

HTTP 요청 URL과 Controller 메서드를 연결한다.

```java
@RestController
@RequestMapping("/members")
class MemberController {

    @GetMapping("/{id}")
    MemberResponse findMember(@PathVariable Long id) {
        return new MemberResponse("member-" + id);
    }

    @PostMapping
    MemberResponse createMember(@RequestBody MemberCreateRequest request) {
        return new MemberResponse(request.name());
    }
}
```

- `@RequestMapping`: 공통 경로나 여러 HTTP method 매핑
- `@GetMapping`: GET 요청
- `@PostMapping`: POST 요청

### `@RequestBody`

HTTP 요청 본문을 Java 객체로 변환한다.

```java
record MemberCreateRequest(String name) {
}

@PostMapping("/members")
MemberResponse create(@RequestBody MemberCreateRequest request) {
    return new MemberResponse(request.name());
}
```

주로 JSON 요청을 DTO로 받을 때 사용한다.

### `@PathVariable`, `@RequestParam`

URL 경로 값과 query parameter를 받을 때 사용한다.

```java
@GetMapping("/members/{id}")
MemberResponse findOne(@PathVariable Long id) {
    return new MemberResponse("member-" + id);
}

@GetMapping("/members")
List<MemberResponse> search(@RequestParam String name) {
    return List.of(new MemberResponse(name));
}
```

- `@PathVariable`: `/members/1`의 `1`
- `@RequestParam`: `/members?name=kim`의 `name`

### `@Valid`

요청 DTO의 검증을 실행한다.

```java
record MemberCreateRequest(
        @NotBlank String name
) {
}

@PostMapping("/members")
MemberResponse create(@Valid @RequestBody MemberCreateRequest request) {
    return new MemberResponse(request.name());
}
```

`@NotBlank`, `@NotNull`, `@Min`, `@Max` 같은 Bean Validation 애노테이션과 함께 사용한다.

### `@ControllerAdvice`, `@ExceptionHandler`

예외를 공통으로 처리할 때 사용한다.

```java
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
```

Controller마다 반복되는 예외 처리 코드를 한곳에 모을 수 있다.

## JPA 관련 애노테이션

### `@Entity`

JPA가 관리하는 Entity 클래스임을 나타낸다.

```java
@Entity
class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    protected Member() {
    }

    Member(String name) {
        this.name = name;
    }
}
```

Entity는 기본 생성자가 필요하다. 보통 외부 생성을 막기 위해 `protected`로 둔다.

### `@Id`, `@GeneratedValue`

Entity의 식별자와 생성 전략을 지정한다.

```java
@Id
@GeneratedValue
private Long id;
```

- `@Id`: 기본 키
- `@GeneratedValue`: 기본 키 값을 자동 생성

### `@Table`, `@Column`

테이블명이나 컬럼 설정을 지정한다.

```java
@Entity
@Table(name = "members")
class Member {

    @Column(name = "member_name", nullable = false, length = 50)
    private String name;
}
```

기본 매핑으로 충분하면 생략할 수 있지만, DB 스키마와 명확히 맞추고 싶을 때 사용한다.

### `@ManyToOne`, `@OneToMany`, `@JoinColumn`

Entity 사이의 연관관계를 매핑한다.

```java
@Entity
class Order {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
```

`Order` 여러 개가 하나의 `Member`에 속할 수 있으므로 `@ManyToOne`을 사용한다.

양방향 관계에서는 반대쪽에 `mappedBy`를 사용할 수 있다.

```java
@Entity
class Member {
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
```

연관관계는 편리하지만 복잡도를 높일 수 있으므로 꼭 필요한 방향만 먼저 잡는 것이 좋다.

## 트랜잭션 관련 애노테이션

### `@Transactional`

메서드나 클래스에 트랜잭션 경계를 만든다.

```java
@Service
class MemberService {
    private final MemberRepository memberRepository;

    MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    Long create(String name) {
        Member member = new Member(name);
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }
}
```

데이터 변경 작업은 보통 트랜잭션 안에서 실행한다.

조회 전용 메서드에는 `readOnly = true`를 사용할 수 있다.

```java
@Transactional(readOnly = true)
MemberResponse findMember(Long id) {
    Member member = memberRepository.findById(id)
            .orElseThrow();

    return new MemberResponse(member.getName());
}
```

## 코드 비교

애노테이션을 사용하지 않으면 객체 생성과 연결을 직접 처리해야 한다.

```java
MemberRepository memberRepository = new MemoryMemberRepository();
MemberService memberService = new MemberService(memberRepository);
MemberController memberController = new MemberController(memberService);
```

Spring 애노테이션을 사용하면 각 클래스의 역할을 표시하고, 의존성 연결은 Spring Container에 맡길 수 있다.

```java
@RestController
class MemberController {
    private final MemberService memberService;

    MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}

@Service
class MemberService {
    private final MemberRepository memberRepository;

    MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}

@Repository
class MemberRepository {
}
```

코드에서 `new`로 직접 조립하던 책임이 Spring Container로 이동한다.

## 동작 흐름

애노테이션 기반 Spring 애플리케이션은 대략 다음 흐름으로 동작한다.

1. Spring Boot가 애플리케이션을 시작한다.
2. `@SpringBootApplication`을 기준으로 컴포넌트 스캔을 수행한다.
3. `@Component`, `@Controller`, `@Service`, `@Repository`가 붙은 클래스를 Bean으로 등록한다.
4. 생성자를 보고 필요한 Bean을 주입한다.
5. `@RestController`와 `@GetMapping` 같은 MVC 애노테이션을 보고 요청 매핑 정보를 만든다.
6. `@Entity`가 붙은 클래스를 JPA Entity로 인식한다.
7. `@Transactional`이 붙은 메서드에는 트랜잭션 처리를 적용한다.

## 주의할 점

애노테이션을 많이 붙인다고 좋은 코드가 되는 것은 아니다.

각 애노테이션이 어떤 계층의 어떤 책임을 표현하는지 알고 사용해야 한다. 예를 들어 `@Service`는 비즈니스 흐름을 처리하는 클래스에 붙이는 것이 자연스럽고, 단순 DTO에는 붙이지 않는다.

Entity와 DTO를 구분하는 것도 중요하다. `@Entity`가 붙은 객체를 API 요청/응답 DTO처럼 사용하면 영속성, JSON 변환, 연관관계 문제가 섞일 수 있다.

`@Transactional`도 아무 곳에나 붙이지 않는다. 보통 Service 계층에서 트랜잭션 경계를 잡고, Controller에는 HTTP 요청/응답 처리 책임을 남겨둔다.

## 정리

Spring 애노테이션은 Spring에게 객체 등록, 의존성 주입, 요청 매핑, 트랜잭션, JPA 매핑 같은 작업을 지시하는 메타데이터다.

중요한 것은 애노테이션 이름을 외우는 것이 아니라, Spring이 그 애노테이션을 보고 어떤 일을 하는지 이해하는 것이다.

## 다음으로 연결되는 주제

이 문서는 자주 쓰는 애노테이션을 빠르게 확인하기 위한 요약 문서다.

이후 실습 프로젝트를 만들면서 각 애노테이션이 실제 요청 처리, DB 저장, 예외 처리 흐름에서 어떻게 동작하는지 확인하면 좋다.
