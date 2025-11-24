-- API 테스트를 위한 초기 데이터 생성 스크립트
-- 사용법: mysql -u root1234 -p street-ball < init-test-data.sql

USE `street-ball`;

-- Court 테이블 먼저 생성해야 함 (선행 조건)
-- DB_schema.sql에서 Court 테이블 생성 부분:

-- Court 테이블 생성
CREATE TABLE IF NOT EXISTS Court (
    court_id INT AUTO_INCREMENT PRIMARY KEY,
    court_name VARCHAR(200) NOT NULL,
    location_lat DOUBLE NOT NULL,
    location_lng DOUBLE NOT NULL,
    is_indoor BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 테스트용 코트 데이터 삽입
INSERT INTO Court (court_name, location_lat, location_lng, is_indoor) VALUES
('강남 농구장', 37.5665, 126.9780, false),
('잠실 체육관', 37.5145, 127.1025, true),
('홍대 스트리트 코트', 37.5572, 126.9239, false),
('송파 실내 체육관', 37.5145, 127.1067, true),
('여의도 한강공원 코트', 37.5285, 126.9341, false);

-- 테스트용 사용자 데이터 (선택사항 - API로 생성 가능)
-- INSERT INTO User (name, location_lat, location_lng, has_ball) VALUES
-- ('김철수', 37.5665, 126.9780, true),
-- ('이영희', 37.5660, 126.9785, false),
-- ('박민수', 37.5670, 126.9775, true);

SELECT '✅ 테스트 데이터 생성 완료!' as Status;
SELECT '📍 생성된 코트 목록:' as Info;
SELECT court_id, court_name, location_lat, location_lng, is_indoor FROM Court;

