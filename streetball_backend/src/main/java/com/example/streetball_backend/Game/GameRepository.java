package com.example.streetball_backend.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    
    // 특정 코트의 게임 목록 조회
    List<Game> findByCourtCourtId(Integer courtId);
    
    // 특정 상태의 게임 목록 조회
    List<Game> findByStatus(GameStatus status);
    
    // 특정 코트 목록에서 진행될 게임 조회 (근처 모임 검색용)
    @Query("SELECT g FROM Game g WHERE g.court.courtId IN :courtIds AND g.status = :status")
    List<Game> findByCourtsAndStatus(
            @Param("courtIds") List<Integer> courtIds,
            @Param("status") GameStatus status);
    
    // 예정된 시간 이후의 게임 조회
    List<Game> findByScheduledTimeAfter(LocalDateTime dateTime);
    
    // 심판이 있는 게임 조회 (모집_중 상태만)
    @Query("SELECT DISTINCT g FROM Game g JOIN Participation p ON g.gameId = p.game.gameId " +
           "WHERE p.role = com.example.streetball_backend.Participation.ParticipationRole.referee " +
           "AND g.status = com.example.streetball_backend.Game.GameStatus.모집_중")
    List<Game> findGamesWithReferee();
    
    // 심판이 없는 게임 조회 (모집_중 상태만)
    @Query("SELECT g FROM Game g WHERE g.status = com.example.streetball_backend.Game.GameStatus.모집_중 " +
           "AND NOT EXISTS (SELECT 1 FROM Participation p WHERE p.game.gameId = g.gameId " +
           "AND p.role = com.example.streetball_backend.Participation.ParticipationRole.referee)")
    List<Game> findGamesWithoutReferee();
}

