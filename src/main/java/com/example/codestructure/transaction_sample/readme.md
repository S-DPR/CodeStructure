# Transaction Sample
※ 해당 코드는 전체 프로젝트 구조를 단순화하여, 구조 설계 방식만 보여주는 예제입니다.
실제 운영 코드는 별도 디렉토리/계층 구성으로 관리됩니다.

## 🧠 목적 (Purpose)

이 프로젝트는 두 개의 독립적인 엔티티(`EntityA`, `EntityB`) 간의 관계를 Foreign Key 등 DB 제약조건 없이 애플리케이션 단에서 직접 관리하고 정합성을 유지하는 구조입니다.

---

## 📐 구조 (Structure)

### Entity 구성

- **EntityA**
    - 고유 UUID로 식별
    - `EntityB` 여러 개가 참조하는 상위 개체
- **EntityB**
    - 고유 UUID로 식별
    - `EntityA`의 UUID(`entityAId`)를 필드로 가짐
    - 단, 실제 FK는 걸려 있지 않음

> 💡 두 Entity는 UUID로만 연결되며, DB 상에서는 참조 제약조건이 존재하지 않음

---

## 🛠 주요 로직 설명

### 저장 로직 (`save`)

1. `EntityA`의 UUID가 없으면 새로 생성
2. `EntityB`에 `EntityA`의 UUID를 연결
3. 두 엔티티를 트랜잭션 내에서 함께 저장

### 삭제 로직 (`delete`)

1. `EntityB`를 삭제
2. 같은 `EntityA`를 참조하는 `EntityB`가 더 이상 없을 경우
3. `EntityA`도 함께 삭제

> 전체 삭제 흐름은 트랜잭션으로 처리되어, 일부 삭제만 이루어지는 상태(데이터 불일치)를 방지합니다.

---

## ✅ 정합성 처리 전략 요약

| 상황                              | 처리 방식 |
|---------------------------------|-----------|
| `EntityA` 미존재                   | UUID 생성 후 저장 |
| `EntityB` 저장 시 `EntityA` 연결     | UUID 필드로 연결 (FK 아님) |
| `EntityB` 삭제 후 정합성 처리           | 남은 `EntityB` 없으면 `EntityA` 삭제 |
| 트랜잭션 내 예외 발생                             | 트랜잭션으로 전체 롤백 |

---

## 💻 기술 스택

- Java 17
- Spring Boot 3
- Spring Data JPA
- Lombok
- PostgreSQL

---

## ✍️ 정리

제약조건이 없는 환경에서 두 개체 간 관계와 정합성을  
애플리케이션 레벨에서 안전하게 처리하는 로직을 설계하고 구현한 샘플입니다.

