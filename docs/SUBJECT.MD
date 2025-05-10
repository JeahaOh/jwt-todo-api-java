# 📝 JWT 기반 TODO 백엔드 API 구현 과제

## 🎯 과제 개요
- SQLite3과 JWT 인증을 기반으로 한 TODO 관리 API를 구현합니다.
- AI Assistant(ChatGPT, Claude 등등 자율) 활용하여 총 12개의 API를 구현하며,
- AI 활용 시 사용한 프롬프트와 테스트 코드를 함께 Githup 템플릿 ZIP파일로 제출해주세요

## ✅ 구현해야 할 API

### 1. 사용자 관련
- POST /users/signup
- POST /users/login
- GET /users/me
- PUT /users/me
- DELETE /users/me
- (옵션, 추가점수) 소셜로그인 구현 및 관련 API 작성

### 2. TODO 관련
- POST /todos
- GET /todos
- GET /todos/{id}
- PUT /todos/{id}
- DELETE /todos/{id}
- GET /todos/search

### 🔐 인증 방식
- 로그인 시 access_token 반환
- 이후 요청은 Authorization: Bearer <token> 헤더를 통해 필요

### 🧪 테스트 실행 (Python 예시)
```bash
  pip install -r requirements.txt
  pytest tests/
```

### ✅ 제출항목

- 전체 소스코드 : Java, Kotlin, Python 중 택 1
- SQLite 초기화 코드
- JWT 발급 및 검증 흐름 다이어그램
  - ✓ (옵션, 추가점수) 위 다이어그램에 OAuth2 흐름
- ○ README.md
  - ✓ 실행방법
  - ✓ API 명세 요약
- ○ 사용한프롬프트
  - ✓ 모든프롬프트는 prompts/used_prompts.md 에 정리
- ○ 테스트코드(필수)
  - ✓ 단위 테스트 또는 통합테스트 작성
  - ✓ 테스트 코드 커버리지 80% 이상

### ✅ 테스트 코드 작성 요구사항

- 필수 테스트 대상
  - 회원가입 -> 로그인 -> JWT 인증 흐름 테스트
  - TODO 생성 -> 목록조회 -> 수정 -> 삭제 테스트
  - JWT 없이 접근시 401 응답 확인
  - 유효하지 않은 ID로 접 근시 404 확인

### ✅ 기타

1. 과제기한 : 3일
2. 제출한 코드는 실행 가능해야하며, 실행되지 않으면 실격입니다
3. AI 프롬프트 포함하여 제출해야하며, 제출하지 않으면 실격입니다
4. AI 프롬프트는 Link로 제출할 수 없으며 used_prompts.md만으로 확인가능하여야 합니다 
