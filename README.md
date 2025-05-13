# 📝 JWT 기반 TODO API (Java + Spring Boot)

SQLite3과 JWT 인증을 기반으로 한 TODO API 백엔드 구현 과제입니다.  
AI Assistant(GPT 등)를 활용하여 전체 구현 및 테스트를 완료하였으며, 사용한 프롬프트는 별도 파일로 정리되어 있습니다.

---

## 과제 진행 결과

- [전체 소스코드 `./app/`](./app/)
- [SQLite 초기화 코드 `./app/src/main/resources/init.sql`](./app/src/main/resources/init.sql)
- [JWT 발급 및 검증 흐름 다이어그램 `./JWT 발급 및 인증 과정.pdf`](./JWT%20발급%20및%20인증%20과정.pdf)
- ○ README.md
  - [실행 방법](#실행-방법)
  - [API 명세 요약](#api-엔드포인트)
- ○ [사용한프롬프트 `./prompts/used_prompts.md`](./prompts/used_prompts.md)
- ○ [테스트코드(필수) `./app/src/test/`](./app/src/test/)
  - ✓ 단위 테스트 또는 통합테스트 작성
  - ✓ 테스트 코드 커버리지 80% 이상

---

## 🧰 기술 스택

### Backend

- Java 17
- Spring Boot 3.3.11
- Spring Security + JWT
    - JWT: jjwt-api 0.11.5
- Spring Data JPA
- SQLite3
    - SQLite JDBC 3.42.0.0
- Gradle

### Testing

- JUnit5
- Mockito
- Spring Test
- Spring Security Test

### Documentation

- Swagger UI
    - springdoc-openapi-starter-webmvc-ui 2.5.0
- OpenAPI 3.0

### Development Tools

- Lombok
- Spring Boot DevTools

---

## 🏗️ 프로젝트 구조

```txt
app/
  ├── src/
  │    ├── main/
  │    │    ├── java/
  │    │    │     └── com/todo/api/
  │    │    │          ├── common/ # 공통 모듈
  │    │    │          ├── mmbr/ # 회원 관리
  │    │    │          └── todo/ # TODO 관리
  │    │    └── resources/
  │    │          └── application.yml # 설정 파일
  │    └── test/ # 테스트 코드
  └── build.gradle # 빌드 설정
```


## 실행 방법

### 1. 사전 요구사항

- Java 17 이상
- Gradle 7.x 이상


### 2. 프로젝트 실행

```bash
# 실행 위치로 이동
cd app

# Gradle 빌드
./gradlew build

# 서버 실행
./gradlew bootRun
```

### 3. 테스트 실행
```bash
# 실행 위치로 이동
cd app

# 전체 테스트 실행
./gradlew test

# 단위 테스트 실행
./gradlew unitTest

# 통합 테스트 실행
./gradlew integrationTest
```

### 4. API 문서 확인

- Swagger UI: http://localhost:8080/swagger-ui.html

---

## 🔐 API 인증

### JWT 인증 흐름

1. 회원가입 (`POST /users/signup`)
2. 로그인 (`POST /users/login`)
3. JWT 토큰 발급
4. API 요청 시 Authorization 헤더에 토큰 포함

### 인증 헤더 형식

`Authorization: Bearer {JWT_TOKEN}`

---

## API 엔드포인트

### 회원 관리

- `POST /users/signup`: 회원가입
- `POST /users/login`: 로그인
- `GET /users/me`: 현재 사용자 정보 조회
- `PUT /users/me`: 사용자 정보 수정
- `DELETE /users/me`: 회원 탈퇴

### TODO 관리

- `POST /todos`: TODO 생성
- `GET /todos`: TODO 목록 조회 (페이징, 정렬 지원)
  - 정렬 기준: createdAt, updatedAt, no, title
  - 정렬 방향: asc, desc
  - 기본 페이지 크기: 10
- `GET /todos/{id}`: 특정 TODO 조회
- `PUT /todos/{id}`: TODO 수정
- `DELETE /todos/{id}`: TODO 삭제
- `GET /todos/search`: TODO 검색
  - 키워드 검색 지원
  - 페이징, 정렬 지원

### 헬스 체크

- `GET /api/health`: 서버 상태, 테이블 수, 응답 시간 확인
- `GET /api/health/error`: 예외 발생 테스트

---
## 🧪 테스트

### 테스트 범위

- 단위 테스트
- 통합 테스트


### 테스트 실행 결과

- 테스트 커버리지: 80% 근접
- 주요 기능 테스트 완료

[테스트 결과 확인 -> ./app/build/reports/tests/test/index.html](./app/build/reports/jacoco/test/html/index.html)


---