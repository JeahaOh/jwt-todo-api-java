# 📝 JWT 기반 TODO API (Java + Spring Boot)

SQLite3과 JWT 인증을 기반으로 한 TODO API 백엔드 구현 과제입니다.  
AI Assistant(GPT 등)를 활용하여 전체 구현 및 테스트를 완료하였으며, 사용한 프롬프트는 별도 파일로 정리되어 있습니다.

---

## 🧰 기술 스택

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- SQLite3
- JUnit5 + Mockito
- Gradle

---

## 🚀 실행 방법

```bash
# 1. Gradle 빌드
./gradlew build

# 2. 서버 실행
./gradlew bootRun

# 3. 테스트 실행
./gradlew test