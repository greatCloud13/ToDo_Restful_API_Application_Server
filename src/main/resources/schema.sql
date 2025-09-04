-- 테이블이 존재하면 삭제
DROP TABLE IF EXISTS todos;
DROP TABLE IF EXISTS users;

-- 테이블 만들기
CREATE TABLE todos(
--    id(할일 ID) : 기본키
ID INT AUTO_INCREMENT PRIMARY KEY,
--  todo(할일): NOT NULL
todo VARCHAR(255) NOT NULL,
-- detail(설명)
detail TEXT,
-- created_at (작성 일자)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
-- updated_at(업데이트 일자)
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);