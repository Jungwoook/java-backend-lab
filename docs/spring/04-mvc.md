# MVC

## 왜 배우는가

백엔드 애플리케이션은 대부분 클라이언트의 HTTP 요청을 받아 처리하고 응답을 반환한다.

Spring MVC는 이 흐름을 구조화해준다. 요청을 받는 Controller, 비즈니스 로직을 처리하는 Service, 데이터를 저장하고 조회하는 Repository를 나누면 각 코드의 책임이 분명해진다.

MVC를 이해하면 REST API를 만들 때 요청과 응답, DTO, 검증, 예외 처리, 계층 분리를 자연스럽게 설계할 수 있다.

## 개념

MVC는 Model, View, Controller의 약자다.

웹 API 중심으로 보면 Spring MVC의 핵심은 요청을 Controller가 받고, 필요한 처리를 Service에 맡기고, 결과를 응답으로 변환하는 흐름이다.

주요 개념은 다음과 같다.

- Controller: HTTP 요청을 받고 응답을 반환한다.
- Service: 비즈니스 흐름과 도메인 로직 호출을 담당한다.
- Repository: 데이터 저장소 접근을 담당한다.
- DTO: 요청과 응답 데이터를 전달하기 위한 객체다.
- Validation: 요청 값이 올바른지 검증한다.
- Exception Handler: 예외를 일관된 응답으로 변환한다.
- REST API: 자원을 URI로 표현하고 HTTP method로 행위를 표현하는 API 스타일이다.

## 핵심

- Controller는 HTTP 계층의 책임에 집중한다.
- Service는 비즈니스 흐름을 조율한다.
- Repository는 데이터 접근을 담당한다.
- Entity를 요청/응답에 직접 노출하기보다 DTO를 사용하는 것이 좋다.
- 검증과 예외 처리는 공통 흐름으로 정리해야 한다.
- REST API는 URI, HTTP method, status code를 일관되게 설계하는 것이 중요하다.

## 코드 비교

나쁜 예로 Controller에 모든 로직이 몰릴 수 있다.

```java
@RestController
class MemberController {

    @PostMapping("/members")
    MemberResponse create(@RequestBody MemberCreateRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        Member member = new Member(request.name());
        // 저장 로직까지 Controller에 들어간다고 가정

        return new MemberResponse(member.getName());
    }
}
```

Controller가 검증, 생성, 저장 흐름까지 모두 알게 되면 책임이 커진다.

계층을 나누면 다음처럼 정리할 수 있다.

```java
@RestController
class MemberController {
    private final MemberService memberService;

    MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    MemberResponse create(@RequestBody MemberCreateRequest request) {
        return memberService.create(request);
    }
}

@Service
class MemberService {
    private final MemberRepository memberRepository;

    MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    MemberResponse create(MemberCreateRequest request) {
        Member member = new Member(request.name());
        Member savedMember = memberRepository.save(member);

        return new MemberResponse(savedMember.getName());
    }
}
```

Controller는 HTTP 요청을 받고 Service를 호출한다. Service는 회원 생성 흐름을 처리하고 Repository를 통해 저장한다.

## 동작 흐름

Spring MVC의 요청 처리 흐름은 다음과 같다.

1. 클라이언트가 HTTP 요청을 보낸다.
2. DispatcherServlet이 요청을 받는다.
3. 요청 URL과 HTTP method에 맞는 Controller 메서드를 찾는다.
4. 요청 본문, path variable, query parameter를 메서드 인자로 변환한다.
5. Controller가 Service를 호출한다.
6. Service가 필요한 비즈니스 흐름을 처리한다.
7. Controller가 응답 객체를 반환한다.
8. Spring MVC가 응답 객체를 JSON 등으로 변환해 클라이언트에게 반환한다.

`@RestController`는 반환 객체를 View가 아니라 HTTP response body로 변환하는 데 자주 사용된다.

## 주의할 점

Controller에 비즈니스 로직을 많이 넣지 않는 것이 좋다.

Controller는 HTTP 요청과 응답을 다루는 계층이다. 할인 계산, 주문 취소 가능 여부, 회원 등급 변경 같은 핵심 규칙은 도메인 객체나 Service 쪽으로 보내는 것이 자연스럽다.

Entity를 API 응답으로 직접 반환하는 것도 조심해야 한다. Entity 구조가 API 스펙으로 굳어지고, 민감한 필드가 노출될 수 있으며, JPA 연관관계가 JSON 변환 과정에서 문제를 만들 수 있다.

예외 처리는 각 Controller마다 반복하기보다 `@ControllerAdvice` 같은 공통 처리 구조로 모으는 것이 좋다.

## 정리

Spring MVC는 HTTP 요청을 받아 애플리케이션 로직으로 연결하고, 결과를 HTTP 응답으로 돌려주는 구조를 제공한다.

Controller, Service, Repository, DTO의 책임을 분리하면 코드가 읽기 쉬워지고 변경에 강해진다. MVC는 이후 JPA를 사용해 실제 데이터베이스와 연결할 때도 기본 골격이 된다.

## 다음으로 연결되는 주제

MVC로 요청과 응답 흐름을 이해했다면, 이제 데이터를 어떻게 저장하고 조회할지 배워야 한다.

다음 주제는 [JPA](05-jpa.md)다.
