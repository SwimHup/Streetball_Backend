package com.example.streetball_backend.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    
    // 게임 단건 조회 (참여자 목록 포함)
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.participations WHERE g.gameId = :gameId")
    Optional<Game> findByIdWithParticipations(@Param("gameId") Integer gameId);
    
    // 모든 게임 조회 (참여자 목록 포함)
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.participations")
    List<Game> findAllWithParticipations();
    
    // 특정 코트의 게임 목록 조회
    List<Game> findByCourtCourtId(Integer courtId);
    
    // 특정 상태의 게임 목록 조회 (참여자 목록 포함)
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.participations WHERE g.status = :status")
    List<Game> findByStatusWithParticipations(@Param("status") GameStatus status);
    
    // 특정 상태의 게임 목록 조회
    List<Game> findByStatus(GameStatus status);
    
    // 특정 코트 목록에서 진행될 게임 조회 (근처 모임 검색용, 참여자 목록 포함)
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.participations WHERE g.court.courtId IN :courtIds AND g.status = :status")
    List<Game> findByCourtsAndStatus(
            @Param("courtIds") List<Integer> courtIds,
            @Param("status") GameStatus status);
    
    // 예정된 시간 이후의 게임 조회
    List<Game> findByScheduledTimeAfter(LocalDateTime dateTime);
    
    // 심판이 있는 게임 조회 (모집_중 상태만, 참여자 목록 포함)
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.participations WHERE g.referee IS NOT NULL " +
           "AND g.status = com.example.streetball_backend.Game.GameStatus.모집_중")
    List<Game> findGamesWithReferee();
    
    // 심판이 없는 게임 조회 (모집_중 상태만, 참여자 목록 포함)
    @Query("SELECT DISTINCT g FROM Game g LEFT JOIN FETCH g.participations WHERE g.referee IS NULL " +
           "AND g.status = com.example.streetball_backend.Game.GameStatus.모집_중")
    List<Game> findGamesWithoutReferee();
}

