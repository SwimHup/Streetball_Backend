-- 1. 사용할 데이터베이스를 선택합니다.
USE `street-ball`;

CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `has_ball` tinyint(1) NOT NULL DEFAULT '0',
  `location_lat` double DEFAULT NULL,
  `location_lng` double DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKgj2fy3dcix7ph7k8684gka40c` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `review` (
  `user_id` int NOT NULL,
  `play_count` int NOT NULL,
  `play_score` decimal(3,2) DEFAULT NULL,
  `ref_count` int NOT NULL,
  `ref_score` decimal(3,2) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `participation` (
  `participation_id` int NOT NULL AUTO_INCREMENT,
  `joined_at` datetime(6) DEFAULT NULL,
  `role` enum('player','referee','spectator') COLLATE utf8mb4_unicode_ci NOT NULL,
  `game_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`participation_id`),
  UNIQUE KEY `uk_game_user` (`game_id`,`user_id`),
  KEY `FKfputwcduinudasn7es02c12ra` (`user_id`),
  CONSTRAINT `FKfputwcduinudasn7es02c12ra` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKlqo9f5gvkcwq0alysxg6wxlxm` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `game_rating` (
  `rating_id` int NOT NULL AUTO_INCREMENT,
  `comment` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `reviewee_role` enum('PLAYER','REFEREE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `game_id` int NOT NULL,
  `reviewee_id` int NOT NULL,
  `reviewer_id` int NOT NULL,
  PRIMARY KEY (`rating_id`),
  UNIQUE KEY `uk_game_reviewer_reviewee` (`game_id`,`reviewer_id`,`reviewee_id`),
  KEY `FKjdegylgmoyr94620tyu6ylpoc` (`reviewee_id`),
  KEY `FKqerfygpv7i4smkfqin8fpffoq` (`reviewer_id`),
  CONSTRAINT `FKasi8t0ujrmp9w4pmy3bb1sjkd` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`),
  CONSTRAINT `FKjdegylgmoyr94620tyu6ylpoc` FOREIGN KEY (`reviewee_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKqerfygpv7i4smkfqin8fpffoq` FOREIGN KEY (`reviewer_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `game` (
  `game_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `current_players` int NOT NULL,
  `location_lat` double DEFAULT NULL,
  `location_lng` double DEFAULT NULL,
  `max_players` int NOT NULL,
  `scheduled_time` datetime(6) NOT NULL,
  `status` enum('게임_종료','모집_완료','모집_중') COLLATE utf8mb4_unicode_ci NOT NULL,
  `court_id` int NOT NULL,
  `referee_id` int DEFAULT NULL,
  PRIMARY KEY (`game_id`),
  KEY `FKjebsrjxrfslf63c9yfq4qy9vp` (`court_id`),
  KEY `FKp0gn1fu7vpadh4xpbsayi609l` (`referee_id`),
  CONSTRAINT `FKjebsrjxrfslf63c9yfq4qy9vp` FOREIGN KEY (`court_id`) REFERENCES `court` (`court_id`),
  CONSTRAINT `FKp0gn1fu7vpadh4xpbsayi609l` FOREIGN KEY (`referee_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci

CREATE TABLE `court` (
  `court_id` int NOT NULL AUTO_INCREMENT,
  `court_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_indoor` tinyint(1) NOT NULL DEFAULT '0',
  `location_lat` double NOT NULL,
  `location_lng` double NOT NULL,
  PRIMARY KEY (`court_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci


-- 5. DELIMITER 설정 변경 및 참여자 추가 Trigger 정의
DELIMITER //

CREATE TRIGGER trg_after_insert_participation
AFTER INSERT ON participation
FOR EACH ROW
BEGIN
    IF NEW.role = 'player' THEN
        UPDATE game
        SET current_players = current_players + 1
        WHERE game_id = NEW.game_id;
    END IF;
    
    -- 최대 인원 초과 시 Game status를 '모집 완료'로 변경
    UPDATE game
    SET status = '모집 완료'
    WHERE game_id = NEW.game_id AND current_players >= max_players AND status = '모집 중';
END //

-- 6. 참여자 삭제 Trigger 정의
CREATE TRIGGER trg_after_delete_participation
AFTER DELETE ON participation
FOR EACH ROW
BEGIN
    IF OLD.role = 'player' THEN
        UPDATE game
        SET current_players = current_players - 1
        WHERE game_id = OLD.game_id;
    END IF;
    
    -- 인원이 최대 인원 미만이 되면 Game status를 '모집 중'으로 변경 (0명보다 많을 때)
    UPDATE game
    SET status = '모집 중'
    WHERE game_id = OLD.game_id AND current_players < max_players AND current_players >= 0 AND status = '모집 완료';
END //

DELIMITER ;