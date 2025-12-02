-- Streetball Backend - MySQL 초기화 스크립트
-- Docker 컨테이너 시작 시 자동으로 실행됩니다.

-- 1. 데이터베이스 선택
USE `street-ball`;

-- 2. User 테이블 생성
CREATE TABLE IF NOT EXISTS User (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `location_lat` decimal(10,8) DEFAULT NULL,
  `location_lng` decimal(11,8) DEFAULT NULL,
  `has_ball` tinyint(1) DEFAULT '0',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Court 테이블 생성
CREATE TABLE IF NOT EXISTS Court (
  `court_id` int NOT NULL AUTO_INCREMENT,
  `court_name` varchar(200) NOT NULL,
  `location_lat` double NOT NULL,
  `location_lng` double NOT NULL,
  `is_indoor` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`court_id`),
  UNIQUE KEY `uk_location` (`location_lat`,`location_lng`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Game 테이블 생성
CREATE TABLE IF NOT EXISTS Game (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    court_id INT NOT NULL,
    max_players INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    current_players INT NOT NULL DEFAULT 0,
    status ENUM('모집 중', '모집 완료', '게임 종료') NOT NULL DEFAULT '모집 중',
    scheduled_time DATETIME NOT NULL,
    FOREIGN KEY (court_id) REFERENCES Court(court_id) ON DELETE CASCADE,
    CHECK (max_players > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Participation 테이블 생성
CREATE TABLE IF NOT EXISTS Participation (
    participation_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    user_id INT NOT NULL,
    role ENUM('player', 'referee', 'spectator') NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_game_user (game_id, user_id),
    FOREIGN KEY (game_id) REFERENCES Game(game_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Review 테이블 생성
CREATE TABLE IF NOT EXISTS Review (
    user_id INT PRIMARY KEY,
    play_score DECIMAL(3, 2) DEFAULT 0.00,
    play_count INT DEFAULT 0,
    ref_score DECIMAL(3, 2) DEFAULT 0.00,
    ref_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. GameRating 테이블 생성
CREATE TABLE IF NOT EXISTS GameRating (
    rating_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    rater_id INT NOT NULL,
    rated_id INT NOT NULL,
    rating_role ENUM('player', 'referee') NOT NULL,
    rating DECIMAL(2, 1) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_game_rater_rated_role (game_id, rater_id, rated_id, rating_role),
    FOREIGN KEY (game_id) REFERENCES Game(game_id) ON DELETE CASCADE,
    FOREIGN KEY (rater_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (rated_id) REFERENCES User(user_id) ON DELETE CASCADE,
    CHECK (rating >= 0 AND rating <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. 기존 트리거 삭제 (있을 경우)
DROP TRIGGER IF EXISTS trg_after_insert_participation;
DROP TRIGGER IF EXISTS trg_after_delete_participation;

-- 9. 참여자 추가 Trigger 정의
DELIMITER //

CREATE TRIGGER trg_after_insert_participation
AFTER INSERT ON Participation
FOR EACH ROW
BEGIN
    IF NEW.role = 'player' THEN
        UPDATE Game
        SET current_players = current_players + 1
        WHERE game_id = NEW.game_id;
    END IF;
    
    -- 최대 인원 초과 시 Game status를 '모집 완료'로 변경
    UPDATE Game
    SET status = '모집 완료'
    WHERE game_id = NEW.game_id AND current_players >= max_players AND status = '모집 중';
END //

-- 10. 참여자 삭제 Trigger 정의
CREATE TRIGGER trg_after_delete_participation
AFTER DELETE ON Participation
FOR EACH ROW
BEGIN
    IF OLD.role = 'player' THEN
        UPDATE Game
        SET current_players = current_players - 1
        WHERE game_id = OLD.game_id;
    END IF;
    
    -- 인원이 최대 인원 미만이 되면 Game status를 '모집 중'으로 변경
    UPDATE Game
    SET status = '모집 중'
    WHERE game_id = OLD.game_id AND current_players < max_players AND current_players >= 0 AND status = '모집 완료';
END //

DELIMITER ;

-- 11. 테스트용 코트 데이터 삽입
INSERT IGNORE INTO Court (court_name, location_lat, location_lng, is_indoor) VALUES
('강남 농구장', 37.56650000, 126.97800000, false),
('잠실 체육관', 37.51450000, 127.10250000, true),
('홍대 스트리트 코트', 37.55720000, 126.92390000, false),
('송파 실내 체육관', 37.51450000, 127.10670000, true),
('여의도 한강공원 코트', 37.52850000, 126.93410000, false);

-- 12. 초기화 완료 메시지
SELECT '✅ 데이터베이스 초기화 완료!' as Status;

