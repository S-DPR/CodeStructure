# CodeStructure Sample Repository

샘플 코드 구조 및 논리를 정리한 레포지토리입니다.  
프로젝트에서 사용했던 설계 패턴이나 문제 해결 방식을 바탕으로, 관련 구조와 흐름을 다른 방식으로 재해결 해본 학습 일지입니다.

> ⚠ 일부 클래스는 구조 설명 목적으로만 존재하며, 전체 실행이 완전하게 보장되지는 않습니다.  
> 전체 로직보다 **구조와 설계의 방향성**에 초점을 두고 있습니다.

## 🧩 구성
```
src/
 └── main/
  └── java/
   └── com.example.codestructure/
    ├── multi_format_parser/
    │ ├── dto/ # 입력 포맷용 Payload 및 추출 데이터 구조
    │ ├── extractors/ # 예시 구체 클래스 
    │ └── readers/ # 포맷을 읽는 기능이 있는 클래스
    ├── transaction_sample/ 
    │ ├── dto/ # 샘플 DTO
    │ ├── entity/ # A-B 관계 Entity 
    │ ├── repos/ # JPA Repository 
    │ └── service/ # 정합성 보장 트랜잭션 로직
  └── test/
     ├── java/
     │   └── com/
     │       └── example/
     │           └── codestructure/
     │               ├── multi_format_parser_test/
     │               │   └── MultiFormatParserTest.java       # 파서 구조 관련 테스트
     │               ├── transaction_sample_test/
     │               │   └── EntityCoordinatorServiceTest.java  # 트랜잭션 정합성 테스트
     │               └── CodeStructureApplicationTests.java     # 기본 부트 테스트
     └── resources/
         └── multi_format_parser_test/ # 예시 데이터
             ├── ExampleA.xml
             ├── ExampleB.xml
             └── ExampleC.json
```

## 📌 주요 구조 설명

### 1. Multi Format Parser Sample

- 다양한 포맷에서 특정 데이터를 추출하기 위한 **추상화 파서 구조**
- `FileExtractor` 추상 클래스 구현 → Dispatcher에서 자동 선택
- 메타데이터 구조가 제각각인 파일의 데이터를 추출/정리하는 데 사용

### 2. Transaction Sample

- 관계형 DB에서 제약 조건 없이 **데이터 정합성 유지 로직 샘플**
- `EntityA ↔ EntityB` 관계에서 삽입/삭제 순서 제어
- 실무에서는 부모-자식 관계 정합성을 서비스 레이어에서 직접 보장한 경험 기반으로 작성

## 🧪 테스트 구조

- `test/resources`에 테스트 XML 포함
- JUnit5 기반의 단위 테스트 일부 작성 (`MultiFormatParserTest`, `EntityCoordinatorServiceTest`)

※ 구조 중심 샘플이므로, 일부 구현은 생략되어 있습니다. 테스트는 구조 이해를 돕기 위한 참고용입니다.


## 📚 기술 스택

- Java 17
- Spring Boot
- JPA / Hibernate
- JUnit5

---

> 이 구조는 제 설계 경험을 바탕으로 구성되었고, 실제 적용된 코드랑은 다름을 알려드립니다.