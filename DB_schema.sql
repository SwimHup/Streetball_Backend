-- 1. 사용할 데이터베이스를 선택합니다.
USE `street-ball`;

CREATE TABLE User (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `location_lat` decimal(10,8) DEFAULT NULL,
  `location_lng` decimal(11,8) DEFAULT NULL,
  `has_ball` tinyint(1) DEFAULT '0',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE Court (
  `court_id` int NOT NULL AUTO_INCREMENT,
  `court_name` varchar(100) NOT NULL,
  `location_lat` decimal(10,8) NOT NULL,
  `location_lng` decimal(11,8) NOT NULL,
  `is_indoor` tinyint(1) NOT NULL,
  PRIMARY KEY (`court_id`),
  UNIQUE KEY `uk_location` (`location_lat`,`location_lng`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

-- 2. Game 테이블 생성
CREATE TABLE Game (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    court_id INT NOT NULL,
    max_players INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    current_players INT NOT NULL DEFAULT 0,
    status ENUM('모집 중', '모집 완료', '게임 종료') NOT NULL DEFAULT '모집 중',
    scheduled_time DATETIME NOT NULL,
    FOREIGN KEY (court_id) REFERENCES Court(court_id) ON DELETE CASCADE,
    CHECK (max_players > 0)
);

-- 3. Participation 테이블 생성
CREATE TABLE Participation (
    participation_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    user_id INT NOT NULL,
    role ENUM('player', 'referee', 'spectator') NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_game_user (game_id, user_id),
    FOREIGN KEY (game_id) REFERENCES Game(game_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- 4. Review 테이블 생성
CREATE TABLE Review (
    user_id INT PRIMARY KEY,
    play_score DECIMAL(3, 2) DEFAULT 0.00,
    play_count INT DEFAULT 0,
    ref_score DECIMAL(3, 2) DEFAULT 0.00,
    ref_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- 5. DELIMITER 설정 변경 및 참여자 추가 Trigger 정의
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

-- 6. 참여자 삭제 Trigger 정의
CREATE TRIGGER trg_after_delete_participation
AFTER DELETE ON Participation
FOR EACH ROW
BEGIN
    IF OLD.role = 'player' THEN
        UPDATE Game
        SET current_players = current_players - 1
        WHERE game_id = OLD.game_id;
    END IF;
    
    -- 인원이 최대 인원 미만이 되면 Game status를 '모집 중'으로 변경 (0명보다 많을 때)
    UPDATE Game
    SET status = '모집 중'
    WHERE game_id = OLD.game_id AND current_players < max_players AND current_players >= 0 AND status = '모집 완료';
END //

DELIMITER ;