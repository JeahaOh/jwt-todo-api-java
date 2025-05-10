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