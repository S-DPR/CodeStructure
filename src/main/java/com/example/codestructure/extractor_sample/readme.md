# FileProcessor Sample

## 🧠 목적 (Purpose)

다양한 포맷(XML, JSON 등)과 구조를 가진 파일로부터 공통된 정보를 추출할 수 있도록 설계된 **확장 가능한 파서 구조**입니다.

포맷과 구조가 다양해도, 전용 파서를 자동 선택하고 공통 로직을 재사용할 수 있도록 추상화 기반으로 설계되었습니다.

---

## 📐 구조 (Architecture)

### Dispatcher

- 파서 목록을 주입받아, 입력된 파일을 분석하고 **적절한 파서를 자동 선택**
- 선택된 파서가 추출한 메타데이터를 반환
- 새로운 구현체가 추가되어도 Dispatcher는 변화하지 않음

### 추상 클래스 계층

```txt
FileProcessor<T>
 └─ XMLFileProcessor (Node 기반)
     └─ XMLFileExtractor (구현체: Landsat8 등)
 └─ JSONFileProcessor (JsonNode 기반)
     └─ JSONFileExtractor (구현체)
```
※ JSONFileProcessor 관련 클래스는 구조 예시용으로만 명시되어 있으며, 샘플 구현은 포함되어 있지 않습니다.

- FileProcessor<T>: 포맷에 관계없이 동작하는 공통 추상 클래스

- XMLFileProcessor: XML 기반 파일 전용 로직 처리

- XMLFileExtractor: 특정 위성 전용 파서 구현체 (예: Landsat8)

---

## ✅ 기능 요약
- 자동 파서 선택	파일을 Dispatcher에 전달하면, 내부적으로 적절한 파서가 자동 선택됨
- 구조 분리	파싱 로직은 각 포맷별 구현체에서만 작성되며, 나머지는 공통 추상화
- 파일 지원 여부 체크	각 파서는 자신이 처리 가능한 파일인지 canHandle()로 판단
- 확장성	새로운 포맷이 추가되어도 추상 구조를 구현하기만 하면 자동 동작 가능

---

## 💻 기술 스택
- Java 17
- Spring Boot 3
- Spring Context (DI)
- DOM XML Parser
- Lombok

---

## 🧪 사용 시나리오
1. 클라이언트가 위성 메타데이터 파일(byte[])을 전달

2. Dispatcher가 형식을 판별하고 적절한 파서를 선택

3. 선택된 파서가 메타데이터 추출 후 DataInfo 반환

---

## 📎 주요 클래스
### Dispatcher.java
→ 파일을 입력받아 파서를 선택하고 메타데이터 추출 수행

### FileProcessor.java
→ 모든 파서의 추상 기반 클래스

### XMLFileProcessor.java
→ XML 전용 추상 처리기 (노드 탐색 등 공통 로직 포함)

### XMLFileExtractor.java
→ 특정 위성 전용 구현체 예시

---

## ✍️ 정리
이 프로젝트는 다양한 포맷/구조를 가진 데이터를 유연하고 확장 가능하게 처리할 수 있는 구조를 구현한 샘플입니다.

- 구조 예측이 어려운 메타데이터 파일
- 포맷별 파싱 로직 분리
- 공통 로직 추상화 및 파서 자동 선택

이런 상황을 깔끔하게 해결할 수 있는 패턴으로, 실제 다양한 포맷 대응 시 유용한 방식입니다.

