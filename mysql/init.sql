-- 초기 데이터베이스 설정
USE conferent;

-- 관리자 사용자 생성 (개발/테스트용)
INSERT INTO users (name, email, password, role, created_at, updated_at) VALUES
('관리자', 'admin@conferent.com', 'admin123', 'ADMIN', NOW(), NOW()),
('홍길동', 'user1@conferent.com', 'user123', 'USER', NOW(), NOW()),
('김철수', 'user2@conferent.com', 'user123', 'USER', NOW(), NOW());

-- 샘플 회의실 데이터
INSERT INTO rooms (name, location, capacity, description, created_at, updated_at) VALUES
('대회의실', '본관 3층', 20, '프레젠테이션 가능한 대형 회의실', NOW(), NOW()),
('소회의실 A', '본관 2층', 6, '팀 미팅용 소규모 회의실', NOW(), NOW()),
('소회의실 B', '본관 2층', 4, '1:1 미팅이나 화상회의용', NOW(), NOW()),
('브레인스토밍룸', '별관 1층', 8, '화이트보드와 포스트잇 완비', NOW(), NOW()),
('임원회의실', '본관 4층', 12, '고급 인테리어의 임원진 전용 회의실', NOW(), NOW());

-- 샘플 예약 데이터 (오늘부터 일주일간)
INSERT INTO reservations (user_id, room_id, start_time, end_time, purpose, status, created_at, updated_at) VALUES
(2, 1, DATE_ADD(NOW(), INTERVAL 2 HOUR), DATE_ADD(NOW(), INTERVAL 4 HOUR), '프로젝트 킥오프 미팅', 'CONFIRMED', NOW(), NOW()),
(3, 2, DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 1 DAY + INTERVAL 1 HOUR), '팀 스프린트 리뷰', 'PENDING', NOW(), NOW()),
(2, 3, DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 2 DAY + INTERVAL 30 MINUTE), '1:1 면담', 'CONFIRMED', NOW(), NOW()); 