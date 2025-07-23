-- MariaDB 운영환경용 초기화 스크립트
-- application-prod.yml에서 hibernate.ddl-auto: validate로 설정되어 있어서
-- 수동으로 실행해야 합니다

-- 데이터베이스 생성 (필요시)
-- CREATE DATABASE IF NOT EXISTS conferent CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 (필요시)
-- CREATE USER IF NOT EXISTS 'conferent_user'@'%' IDENTIFIED BY 'conferent_password';
-- GRANT ALL PRIVILEGES ON conferent.* TO 'conferent_user'@'%';
-- FLUSH PRIVILEGES;

-- 기본 관리자 계정 (운영환경에서는 보안을 위해 비밀번호를 변경해야 함)
INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
('시스템 관리자', 'admin@conferent.com', 'admin123', 'ADMIN', NOW(), NOW()),
('운영 관리자', 'operator@conferent.com', 'operator123', 'ADMIN', NOW(), NOW());

-- 기본 회의실 데이터
INSERT INTO rooms (name, location, capacity, description, created_at, updated_at) VALUES
('대회의실', '본관 3층', 20, '프레젠테이션 가능한 대형 회의실', NOW(), NOW()),
('소회의실 A', '본관 2층', 6, '팀 미팅용 소규모 회의실', NOW(), NOW()),
('소회의실 B', '본관 2층', 4, '1:1 미팅이나 화상회의용', NOW(), NOW()),
('브레인스토밍룸', '별관 1층', 8, '화이트보드와 포스트잇 완비', NOW(), NOW()),
('임원회의실', '본관 4층', 12, '고급 인테리어의 임원진 전용 회의실', NOW(), NOW()),
('교육실 A', '별관 2층', 15, '교육 및 세미나용', NOW(), NOW()),
('교육실 B', '별관 2층', 15, '교육 및 세미나용', NOW(), NOW()),
('미디어룸', '본관 1층', 10, '영상회의 및 미디어 시청용', NOW(), NOW()),
('VIP 회의실', '본관 5층', 8, '고객사 미팅 전용 회의실', NOW(), NOW()),
('세미나홀', '별관 3층', 50, '대규모 세미나 및 발표용', NOW(), NOW());

-- 샘플 예약 데이터 (운영환경에서는 실제 데이터로 대체)
INSERT INTO rents (start_time, end_time, purpose, description, user_id, created_at, updated_at) VALUES
-- 오늘 예약
(NOW() + INTERVAL 2 HOUR, NOW() + INTERVAL 4 HOUR, '시스템 점검', '월간 시스템 점검 회의', 1, NOW(), NOW()),
(NOW() + INTERVAL 6 HOUR, NOW() + INTERVAL 7 HOUR, '운영 회의', '일일 운영 상황 점검', 2, NOW(), NOW()),

-- 내일 예약
(NOW() + INTERVAL 1 DAY + INTERVAL 10 HOUR, NOW() + INTERVAL 1 DAY + INTERVAL 12 HOUR, '보안 교육', '전사 보안 교육', 1, NOW(), NOW()),
(NOW() + INTERVAL 1 DAY + INTERVAL 14 HOUR, NOW() + INTERVAL 1 DAY + INTERVAL 16 HOUR, '고객사 미팅', '신규 고객사 제안 미팅', 2, NOW(), NOW()),

-- 모레 예약
(NOW() + INTERVAL 2 DAY + INTERVAL 9 HOUR, NOW() + INTERVAL 2 DAY + INTERVAL 11 HOUR, '분기별 회의', 'Q1 분기별 성과 회의', 1, NOW(), NOW()),
(NOW() + INTERVAL 2 DAY + INTERVAL 13 HOUR, NOW() + INTERVAL 2 DAY + INTERVAL 15 HOUR, '기술 세미나', '신기술 도입 세미나', 2, NOW(), NOW());

-- 회의실-예약 연결 데이터
INSERT INTO room_rents (room_id, rent_id, created_at) VALUES
(1, 1, NOW()), -- 대회의실 - 시스템 점검
(2, 2, NOW()), -- 소회의실 A - 운영 회의
(6, 3, NOW()), -- 교육실 A - 보안 교육
(9, 4, NOW()), -- VIP 회의실 - 고객사 미팅
(1, 5, NOW()), -- 대회의실 - 분기별 회의
(10, 6, NOW()); -- 세미나홀 - 기술 세미나

-- 샘플 사용자 초대 데이터
INSERT INTO user_invites (user_id, room_rent_id, status, invited_at, responded_at, created_at, updated_at) VALUES
-- 시스템 점검 회의 초대
(2, 1, 'ACCEPTED', NOW(), NOW() + INTERVAL 1 HOUR, NOW(), NOW()),

-- 보안 교육 초대
(2, 3, 'ACCEPTED', NOW(), NOW() + INTERVAL 1 DAY, NOW(), NOW()),

-- 분기별 회의 초대
(2, 5, 'ACCEPTED', NOW(), NOW() + INTERVAL 1 DAY, NOW(), NOW()); 