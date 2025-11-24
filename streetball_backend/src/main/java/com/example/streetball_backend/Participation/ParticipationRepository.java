package com.example.streetball_backend.Participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    
    // 특정 게임의 참여자 목록 조회
    List<Participation> findByGameGameId(Integer gameId);
    
    // 특정 유저의 참여 내역 조회
    List<Participation> findByUserUserId(Integer userId);
    
    // 특정 게임의 특정 역할 참여자 조회
    List<Participation> findByGameGameIdAndRole(Integer gameId, ParticipationRole role);
    
    // 특정 게임에 특정 유저가 이미 참여했는지 확인
    @Query("SELECT p FROM Participation p WHERE p.game.gameId = :gameId AND p.user.userId = :userId")
    Optional<Participation> findByGameIdAndUserId(
            @Param("gameId") Integer gameId,
            @Param("userId") Integer userId);
    
    // 중복 참여 체크
    boolean existsByGameGameIdAndUserUserId(Integer gameId, Integer userId);
}

