-- 사용자 테이블
CREATE TABLE users (
    student_id VARCHAR(20) PRIMARY KEY,         -- 학번 (필수)
    department VARCHAR(50) NOT NULL,             -- 학부 (필수)
    school_year INT NOT NULL,                    -- 학년 (필수)
    major VARCHAR(50) NOT NULL,                  -- 학과 (필수)
    gender VARCHAR(10) NOT NULL,                 -- 성별 (필수)
    name VARCHAR(30) NOT NULL,                   -- 이름 (필수)
    phone VARCHAR(20) NOT NULL,                  -- 전화번호 (필수)
    profile_image BLOB,                          -- 프로필 이미지 (선택)
    user_id VARCHAR(30) NOT NULL UNIQUE,         -- 아이디 (필수)
    password VARCHAR(100) NOT NULL,              -- 비밀번호 (필수)
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,         -- 가입일 (자동 입력)
    is_deleted BOOLEAN DEFAULT FALSE,            -- 탈퇴 여부 (기본 false)
    is_verified BOOLEAN DEFAULT FALSE            -- 학교 인증 여부 (기본 false)
);

-- 채팅 테이블
CREATE TABLE chats (
    chat_id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- 채팅 ID
    sender_id VARCHAR(20) NOT NULL,                 -- 보낸 사람 (필수)
    receiver_id VARCHAR(20) NOT NULL,               -- 받은 사람 (필수)
    message TEXT NOT NULL,                          -- 메시지 내용 (필수)
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 보낸 시간 (자동 입력)
    FOREIGN KEY (sender_id) REFERENCES users(student_id),
    FOREIGN KEY (receiver_id) REFERENCES users(student_id)
);



-- 친구 테이블
CREATE TABLE friends (
    user_id VARCHAR(20) NOT NULL,                     -- 사용자 (필수)
    friend_id VARCHAR(20) NOT NULL,                   -- 친구 (필수)
    status VARCHAR(20) NOT NULL,                      -- 상태 (필수)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,    -- 생성일 (자동 입력)
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(student_id),
    FOREIGN KEY (friend_id) REFERENCES users(student_id)
);

-- 알림 테이블
CREATE TABLE notifications (
    notification_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 알림 ID
    recipient_id VARCHAR(20) NOT NULL,         -- 받는 사람 (필수)
    content TEXT NOT NULL,                     -- 알림 내용 (필수)
    is_read BOOLEAN DEFAULT FALSE,             -- 읽음 여부 (기본 false)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일 (자동 입력)
    FOREIGN KEY (recipient_id) REFERENCES users(student_id)
);



-- 퀴즈 테이블 (학교 인증용)
CREATE TABLE quiz_questions (
    quiz_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 문제 ID
    question TEXT NOT NULL,                    -- 문제 내용 (필수)
    answer VARCHAR(100) NOT NULL                -- 정답 (필수)
);

-- 매칭 테이블 (기록)
CREATE TABLE matches (
    match_id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 매칭 고유 ID
    user1_id VARCHAR(20) NOT NULL,               -- 사용자1
    user2_id VARCHAR(20) NOT NULL,               -- 사용자2
    school_year INT,                             -- 매칭된 학년
    major VARCHAR(50),                           -- 매칭된 학과
    status VARCHAR(20) DEFAULT 'WAITING',        -- 상태(대기, 매칭완료 등)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성 시각
    FOREIGN KEY (user1_id) REFERENCES users(student_id),
    FOREIGN KEY (user2_id) REFERENCES users(student_id)
);