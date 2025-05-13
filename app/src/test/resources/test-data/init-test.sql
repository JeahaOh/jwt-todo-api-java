-- 테이블 초기화
DELETE FROM
  todo;

DELETE FROM
  member;

-- 테스트용 회원 데이터
INSERT INTO
  member (
    no,
    email,
    name,
    password,
    created_at,
    updated_at
  )
VALUES
  (
    1,
    'test1@example.com',
    '테스트1',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    2,
    'test2@example.com',
    '테스트2',
    '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );

-- 테스트용 Todo 데이터
INSERT INTO
  todo (
    no,
    title,
    description,
    completed,
    member_no,
    created_at,
    updated_at
  )
VALUES
  (
    1,
    '첫 번째 할 일',
    '첫 번째 할 일 설명입니다.',
    false,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    2,
    '두 번째 할 일',
    '두 번째 할 일 설명입니다.',
    true,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    3,
    '세 번째 할 일',
    '세 번째 할 일 설명입니다.',
    false,
    2,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );