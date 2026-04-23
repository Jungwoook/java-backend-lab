# JPA

## 왜 배우는가

백엔드 애플리케이션은 대부분 데이터를 데이터베이스에 저장하고 다시 조회한다.

Java 객체와 관계형 데이터베이스는 구조가 다르다. 객체는 참조로 관계를 맺고, 데이터베이스는 테이블과 외래 키로 관계를 표현한다. 이 차이를 직접 SQL로만 다루면 반복 코드가 많아지고 객체지향적인 설계가 흐려질 수 있다.

JPA는 객체와 관계형 데이터베이스 사이의 차이를 줄여주는 ORM 표준이다.

## 개념

JPA는 Java Persistence API의 줄임말이다.

JPA 자체는 표준 인터페이스이고, 실제 구현체로는 Hibernate가 많이 사용된다. Spring에서는 보통 Spring Data JPA를 함께 사용해 Repository 구현을 더 간단하게 만든다.

주요 개념은 다음과 같다.

- ORM: 객체와 관계형 데이터베이스를 매핑하는 기술
- Entity: 데이터베이스 테이블과 매핑되는 객체
- EntityManager: Entity를 저장, 조회, 수정, 삭제하는 핵심 객체
- Persistence Context: Entity를 관리하는 영속성 컨텍스트
- Transaction: 데이터 변경 작업을 하나의 작업 단위로 묶는 것
- Association Mapping: 객체 관계를 DB 관계와 연결하는 매핑
- Lazy Loading: 실제 필요할 때 연관 데이터를 조회하는 방식
- JPQL: Entity 객체를 대상으로 작성하는 객체지향 쿼리

## 핵심

- JPA는 SQL 중심이 아니라 객체 중심으로 데이터를 다루게 해준다.
- Entity는 단순 DTO가 아니라 도메인 규칙을 가질 수 있는 객체다.
- 영속성 컨텍스트는 Entity를 추적하고 변경 감지를 수행한다.
- 데이터 변경은 트랜잭션 안에서 이루어져야 한다.
- 연관관계 매핑은 방향과 주인을 명확히 이해해야 한다.
- 지연 로딩은 성능에 유리하지만 N+1 문제를 만들 수 있다.
- Spring Data JPA는 Repository 구현을 자동으로 만들어준다.

## 코드 비교

JDBC나 직접 SQL 중심 코드에서는 저장 로직이 반복되기 쉽다.

```java
String sql = "insert into member(name) values (?)";

PreparedStatement statement = connection.prepareStatement(sql);
statement.setString(1, member.getName());
statement.executeUpdate();
```

JPA를 사용하면 Entity를 저장하는 방식으로 표현할 수 있다.

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

Spring Data JPA Repository를 사용하면 기본 CRUD 구현을 직접 작성하지 않아도 된다.

```java
interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
}
```

Service에서는 Repository를 사용해 Entity를 저장한다.

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

JPA는 객체를 저장하는 방식으로 코드를 작성하게 해주고, 내부에서 필요한 SQL을 생성해 실행한다.

## 동작 흐름

JPA의 기본 저장 흐름은 다음과 같다.

1. Service 메서드가 트랜잭션 안에서 실행된다.
2. 새 Entity 객체를 생성한다.
3. Repository의 `save()`를 호출한다.
4. Entity가 영속성 컨텍스트에서 관리된다.
5. 트랜잭션이 커밋된다.
6. JPA가 필요한 SQL을 데이터베이스에 반영한다.

수정 흐름에서는 변경 감지가 중요하다.

1. 트랜잭션 안에서 Entity를 조회한다.
2. 조회된 Entity는 영속성 컨텍스트가 관리한다.
3. Entity의 상태를 변경한다.
4. 트랜잭션 커밋 시점에 JPA가 변경 내용을 감지한다.
5. 필요한 update SQL이 실행된다.

## 주의할 점

JPA를 사용해도 데이터베이스와 SQL을 몰라도 되는 것은 아니다.

JPA는 SQL을 대신 만들어주지만, 결국 데이터베이스 위에서 동작한다. 테이블 구조, 인덱스, 트랜잭션, 쿼리 성능을 함께 이해해야 한다.

연관관계 매핑은 특히 조심해야 한다. 양방향 연관관계를 무분별하게 만들면 객체 그래프가 복잡해지고, JSON 변환이나 조회 성능에서 문제가 생길 수 있다.

지연 로딩을 사용할 때는 N+1 문제를 주의해야 한다. 목록 조회 후 각 Entity의 연관 데이터를 하나씩 조회하면 예상보다 많은 SQL이 실행될 수 있다.

Entity를 API 요청/응답 DTO로 직접 사용하지 않는 것도 중요하다. Entity는 영속성, 도메인 규칙, DB 매핑과 관련된 객체이고, API DTO는 외부와 데이터를 주고받기 위한 객체다.

## 정리

JPA는 객체와 관계형 데이터베이스를 매핑해주는 ORM 표준이다.

Entity, 영속성 컨텍스트, 트랜잭션, 연관관계 매핑, 지연 로딩을 이해해야 JPA를 안정적으로 사용할 수 있다. Spring Data JPA는 Repository 구현을 줄여주지만, JPA의 기본 동작 원리를 알고 있어야 문제를 해결할 수 있다.

## 다음으로 연결되는 주제

JPA까지 학습하면 Java와 Spring 백엔드의 큰 흐름을 한 번 훑은 것이다.

다음에는 [Spring Annotation 요약](06-spring-annotations.md)을 보면서 자주 쓰는 애노테이션을 빠르게 정리하고, 작은 CRUD API 프로젝트를 만들면서 Spring Boot, MVC, JPA를 함께 실습하는 것이 좋다.
