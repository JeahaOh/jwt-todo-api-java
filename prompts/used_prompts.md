# 작업시 사용 프롬프트

---
## 요청
.gitignore 파일 생성  
mac os, windows os, linux os,  
visual studio code, intellij, cursor IDE,  
java, spring boot, swagger, docker, sql lite3  

## 응답

```markdown
아래는 요청하신 환경(MacOS, Windows, Linux, VS Code, IntelliJ, Cursor IDE, Java, Spring Boot, Swagger, Docker, SQLite3)을 모두 고려한 .gitignore 파일입니다. 보편적이며 안전한 무시 규칙들을 포함하고 있으며, 실수로 커밋되기 쉬운 개발 툴 설정, OS 캐시, 빌드 아티팩트, 민감 파일 등을 철저히 방지합니다.

⸻

✅ .gitignore 전체 예제

##########################################
# OS 관련 무시 항목
##########################################

# macOS
.DS_Store
.AppleDouble
.LSOverride
Icon?
._*
.Spotlight-V100
.Trashes
__MACOSX/

# Windows
Thumbs.db
ehthumbs.db
Desktop.ini
$RECYCLE.BIN/
*.lnk

# Linux
*~

##########################################
# IDE 및 에디터
##########################################

# VS Code
.vscode/
.history/

# IntelliJ IDEA
.idea/
*.iws
*.iml
*.ipr
out/

# Cursor IDE (기본적으로 VSCode 기반이나, 별도 캐시 파일 있을 경우 대비)
.cursor/

##########################################
# Java & Spring Boot 관련
##########################################

# Maven
target/
*.class

# Gradle
.gradle/
build/
!gradle-wrapper.jar

# Java 일반
*.log
*.ctxt
hs_err_pid*

# Spring Boot 실행 파일
*.jar
*.war
*.ear
*.nar
*.zip
*.tar.gz
*.rar

# JVM 에러 덤프
replay_pid*

# 특정 툴 관련
.mtj.tmp/

##########################################
# Swagger
##########################################

swagger-ui/
swagger-ui.html
api-docs.json
api-docs.yaml

##########################################
# Docker 관련
##########################################

*.env
.env.*
.dockerignore
Dockerfile*
docker-compose.yml
docker-compose.override.yml

##########################################
# SQLite3
##########################################

*.db
*.db3
*.sqlite
*.sqlite3
*.db-journal
*.sqlite-journal

##########################################
# 민감 정보 및 환경 변수
##########################################

*.pem
*.crt
*.key
*.jks
*.keystore
*.p12
*.der

# Spring Profiles
application-local.yml
application-local.properties

##########################################
# 기타 캐시, 백업, 임시 파일
##########################################

*.tmp
*.bak
*.swp
*.swo
*.orig
*.rej

# Mac 압축 시 생성 파일
__MACOSX/

⸻

📌 추가 팁
	•	팀원과 IDE 설정을 공유해야 한다면, .vscode/settings.json 또는 .idea/codeStyles 등의 예외만 수동으로 .gitignore에서 제거하면 됩니다.
	•	**Swagger 스펙 파일(yaml/json)**은 자동 생성되는 경우 무시하는 게 일반적이지만, 수동 작성 시 .gitignore에서 제외해야 합니다.

⸻

원하시면 이 설정을 자동으로 생성해주는 .editorconfig 또는 .gitattributes 파일도 같이 도와드릴까요?
```

---

## 요청

사용자 관련 API와 TODO 관련 API를 RESTfull 한 설계를 하여 Backend application을 만들 예정. build.gradle의 dependencies에 들어갈 의존성 목록을 리스트업 해줘

- java spring boot, 패키지 관리와 빌드 도구로 Gradle
- spring boot web, jpa, security, validation, webmvc, web services, dev tools
- JWT를 사용하여 보안 로그인,
- SQLite3, JPA, hibernate, dialects
- lombok, OpenAPI ui(swagger)

## 응답

아래는 요청하신 내용을 충실히 반영한 RESTful 백엔드 Spring Boot 애플리케이션용 build.gradle.kts 의존성 목록입니다. SQLite, JWT, Hibernate Dialect, Swagger(OpenAPI), Lombok 등 모두 포함되어 있습니다.

⸻

### ✅ build.gradle.kts – 최종 의존성 목록

```
plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23" // Java만 쓸 경우 제거 가능
    kotlin("plugin.spring") version "1.9.23" // Java만 쓸 경우 제거 가능
}

dependencies {
    // 🌐 Spring Web & REST
    implementation("org.springframework.boot:spring-boot-starter-web") // REST API
    implementation("org.springframework.boot:spring-boot-starter-webmvc") // MVC 구조

    // 🛠 Dev Tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // 🛡 Security & Validation
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // 🧾 JPA & Hibernate
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final") // Hibernate Core 직접 명시
    implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final") // 다양한 DB dialects

    // 🗃 SQLite JDBC
    implementation("org.xerial:sqlite-jdbc:3.45.1.0") // 최신 안정화 버전 확인 필요

    // 🔐 JWT (Java Web Token)
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // 🧱 Lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // 📚 Swagger (OpenAPI UI)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // 🧪 Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

⸻

### 📌 추가 설명

Hibernate Dialect 설정
	•	hibernate-community-dialects는 SQLite 같은 비공식 DB 지원을 위해 필요하며,
	•	application.yml 또는 application.properties에서 아래처럼 사용합니다:

```
spring:
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
    show-sql: true
  datasource:
    url: jdbc:sqlite:./data/todo.db
    driver-class-name: org.sqlite.JDBC
```
⸻

이 구성은 사용자 인증(JWT 기반) + TODO CRUD API를 구현하는 RESTful Spring Boot 백엔드 프로젝트에 최적화되어 있습니다.

필요하시면 JWT 필터 구성, Security 설정, Swagger 문서 설정, Entity 및 API 예시도 바로 도와드릴 수 있습니다. 어떤 부분부터 작업하시겠어요?