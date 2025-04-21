# Multi-Format File Parser Sample

※ 이 코드는 다양한 포맷(XML/JSON)의 파일을 처리하는 구조를 단순화한 예제입니다.  
실제 운영 환경에서는 예외 처리 강화, 로그 추가 등이 필요합니다.

---

## 🧠 목적 (Purpose)

여러 포맷(XML, JSON 등)의 파일을 하나의 파싱 시스템에서 통합적으로 처리하기 위해 확장 가능한 인터페이스 기반의 구조를 설계하고, 포맷에 따라 각기 다른 파서가 작동하도록 구현한 예제입니다.


---
## 🧪 테스트 코드

> 주요 테스트: 파싱 가능한 File을 넣었을 때 정상 작동하는지 확인 및 실패 테스트 (성공/실패 케이스 분리 확인 가능)

[테스트 코드 바로가기](https://github.com/S-DPR/CodeStructure/blob/master/src/test/java/com/example/codestructure/multi_format_parser_test/MultiFormatParserTest.java)

---

## 📐 구조 (Structure)

### 핵심 구성 요소

- **FormatPayloadFactory**
    - 바이트 배열 데이터를 받아 XML, JSON으로 파싱 시도
    - 포맷별 파싱 실패 시 `null`로 처리

- **FormatPayload**
    - XML/JSON 노드를 포함한 전달 객체
    - 각 Extractor는 이를 기준으로 파싱 수행

- **FileExtractor (Interface)**
    - 각 파일 포맷별 파싱 로직 정의
    - `canHandle()` 메서드로 해당 포맷 처리 가능 여부 판단
    - `process()` 메서드로 실제 데이터 추출

- **DataExtractionService**
    - 입력된 파일을 포맷 감지 후, 처리 가능한 Extractor 선택
    - 선택된 Extractor로부터 DataInfo 추출

---

## 🛠 예시 구현 클래스 설명

### `ExampleAExtractor`
- XML 기반 파서
- 루트에서 `NAME`, `FILENAME` 태그를 읽어옴
- `NAME`에 있는 값으로 자신이 처리할 수 있는 파일인지 판단

### `ExampleBExtractor`
- XML 기반 파서
- `PROPERTIES/FIRST_NAME`, `LAST_NAME`, `FILENAME` 읽어 조합
- `FIRST_NAME`과 `LAST_NAME`을 합쳐 자신이 처리할 수 있는 파일인지 판단 

### `ExampleCExtractor`
- JSON 기반 파서
- JSON의 `NAME`, `FILENAME` 필드 추출
- `NAME`에 있는 값으로 자신이 처리할 수 있는 파일인지 판단

> 💡 하나의 파일에 대해 두 개 이상의 canHandle에서 true가 나오지 않도록 구현 필요

---

## ✅ 처리 흐름 요약

| 단계                     | 처리 내용 |
|------------------------|-----------|
| 파일 입력                | 바이트 배열 형태 |
| 포맷 감지               | XML 또는 JSON 파싱 |
| Extractor 탐색           | `canHandle()` 기준으로 처리 가능한 하나의 Extractor 선택 |
| 데이터 추출             | 선택된 Extractor의 `process()` 수행 |
| 예외 또는 매칭 실패      | 빈 `DataInfo` 객체 반환 및 로그 출력 |

---

## 💻 기술 스택

- Java 17
- Spring Boot 3
- Lombok
- Jackson (JSON 파싱)
- W3C DOM (XML 파싱)

---

## ✍️ 정리

XML/JSON 같이 다양한 포맷의 파일을 유연하게 처리할 수 있는 구조를 설계하고,  
각 포맷마다 독립적으로 파싱 로직을 분리하여 유지보수성과 확장성을 확보한 샘플입니다.  
