-- init.sql
DROP TABLE IF EXISTS todo;

DROP TABLE IF EXISTS member;

CREATE TABLE member (
  no INTEGER PRIMARY KEY AUTOINCREMENT,
  email TEXT NOT NULL UNIQUE,
  name TEXT NOT NULL,
  password TEXT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- t3st!23$
INSERT INTO
  member (email, name, password)
VALUES
  (
    'test1@test.com',
    'test1',
    '$2a$10$LQd2vz3DJ/V/2QqvYL9GKeW/UhsnpDESQauh/NMho2gKumFv354SS'
  ),
  (
    'test2@test.com',
    'test2',
    '$2a$10$LQd2vz3DJ/V/2QqvYL9GKeW/UhsnpDESQauh/NMho2gKumFv354SS'
  ),
  (
    'test3@test.com',
    'test3',
    '$2a$10$LQd2vz3DJ/V/2QqvYL9GKeW/UhsnpDESQauh/NMho2gKumFv354SS'
  );

CREATE TABLE todo (
  no INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  description TEXT,
  completed INTEGER NOT NULL DEFAULT 0,
  member_no INTEGER NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (member_no) REFERENCES member(no)
);

INSERT INTO
  todo (title, description, member_no)
VALUES
  -- member_no = 1 (쉬는 날 할 일)
  ('☀️ 햇빛 쬐기', '공원에서 햇빛 받으면서 산책하기', 1),
  ('🛁 반신욕 즐기기', '아로마 오일 넣고 피로 풀기', 1),
  ('🎮 게임 몰입하기', '하루 종일 하고 싶었던 게임 달리기', 1),
  ('🍿 영화 몰아보기', '보고 싶었던 넷플릭스 시리즈 정주행', 1),
  ('🧘‍♂️ 명상하기', '앱 틀어놓고 10분 집중 명상', 1),
  ('☕ 카페 가기', '근처 카페에서 여유롭게 커피 한 잔', 1),
  ('📸 사진 찍기', '스냅 사진 찍으며 힐링', 1),
  ('🎧 음악 감상', '오랜만에 좋아하는 앨범 통째로 듣기', 1),
  ('📖 웹툰 보기', '찜해둔 웹툰 몰아보기', 1),
  ('🛌 낮잠 자기', '알람 없이 푹 자기', 1),
  -- member_no = 2 (개인 할 일)
  ('📚 독서하기', '읽다 만 책 끝내기', 2),
  ('🧹 집 청소하기', '방, 화장실, 주방 정리하기', 2),
  ('✍️ 일기 쓰기', '하루 되돌아보며 글 쓰기', 2),
  ('🧺 빨래 돌리기', '빨래하고 널기까지', 2),
  ('🍽️ 요리하기', '새로운 레시피 도전', 2),
  ('💪 운동하기', '홈트 또는 러닝 30분 이상', 2),
  ('🛍️ 장보기', '필요한 생필품 구매', 2),
  ('💡 아이디어 정리', '노션에 메모 적기', 2),
  ('💻 개인 프로젝트', '사이드 프로젝트 코드 정리', 2),
  ('📦 택배 정리', '도착한 택배 정리하고 쓰레기 버리기', 2),
  -- member_no = 3 (회사 업무)
  ('🧑‍💻 이메일 확인', '아침에 전체 메일 체크', 3),
  ('📈 일일 보고 작성', '전날 작업 내용 기록', 3),
  ('🗂️ 문서 정리', '공유드라이브 문서 구조 정리', 3),
  ('🧾 회의록 정리', '금일 회의 내용 정리 및 공유', 3),
  ('📊 데이터 분석', '사용자 행동 데이터 정리', 3),
  ('🛠️ 버그 수정', '긴급 이슈 해결', 3),
  ('🧪 QA 테스트', '신규 기능 QA 체크', 3),
  ('🧑‍💻 코드 리뷰', '팀원 PR 검토', 3),
  ('📅 스프린트 회의', '업무 우선순위 조정', 3),
  ('📝 회의 준비', '자료 정리, 발표 슬라이드 작성', 3),
  ('🔍 로그 확인', '에러 로그 분석', 3),
  ('🌐 페이지 반응속도 체크', 'Lighthouse로 측정', 3),
  ('🔒 보안 점검', 'JWT 만료 정책 확인', 3),
  ('🎯 OKR 업데이트', '분기별 목표 점검', 3),
  ('📦 릴리즈 정리', '버전 태깅 및 노트 작성', 3),
  ('👥 팀 회의 참석', '주간 공유 미팅', 3),
  ('🧮 성능 모니터링', '서버 자원 체크', 3),
  ('🔧 설정 변경', '환경 설정값 수정 반영', 3),
  ('📌 트러블슈팅', '고객 이슈 재현 및 해결', 3),
  ('📋 작업 계획 수립', '이번 주 업무 계획 수립', 3),
  ('📐 UI 점검', '디자인과 맞는지 체크', 3),
  ('📤 업무 공유', '업무 진행 상황 보고', 3),
  ('🔄 Git 리베이스', '브랜치 정리', 3),
  ('🧭 백로그 정리', '티켓 정리 및 우선순위 선정', 3),
  ('📘 API 문서 갱신', 'Swagger 최신화', 3),
  ('🧩 모듈 통합 테스트', '컴포넌트 연결 확인', 3),
  ('🖇️ 라이브러리 업데이트', 'npm 패키지 정리', 3),
  ('🧱 DB 마이그레이션', '스키마 변경 반영', 3),
  ('📎 피드백 반영', '클라이언트 요청 수정', 3),
  ('🗃️ 데이터 정규화', 'DB 구조 정리', 3),
  ('🛎️ 운영자 알림 설정', 'Slack Webhook 연결', 3),
  ('🛡️ 보안 로그 확인', '접근 기록 및 로그인 분석', 3),
  ('🧾 테스트 케이스 작성', '단위 테스트 추가', 3),
  ('🖋️ 컨벤션 리뷰', '코딩 스타일 체크', 3),
  ('📊 GA 이벤트 점검', '이벤트 누락 확인', 3),
  ('📌 신규 기능 브리핑', '팀원 대상 공유', 3),
  ('📥 인입 메일 처리', '지원팀 전달사항 처리', 3),
  ('📃 커밋 로그 정리', '의미 있는 메시지로 수정', 3),
  ('🛳️ 배포 일정 조율', '릴리즈 일정 팀과 협의', 3),
  ('🧭 신규 플로우 설계', '유저 시나리오 작성', 3),
  ('🎬 업무 마무리 정리', '오늘 작업 내역 정리', 3),
  ('🛠️ 유지보수 요청 대응', '운영 중 기능 문의 처리', 3),
  ('💬 채널 응답하기', 'Slack 문의 답변', 3),
  ('💼 신규 요청 정리', '기획 요청 내용 검토', 3),
  ('🔍 테스트 결과 확인', '기능 별 테스트 결과 확인', 3),
  ('📤 서버 재배포', '최신 코드 적용 후 배포', 3);