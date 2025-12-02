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
    
    // Game과 User 엔티티로 중복 참여 체크
    boolean existsByGameAndUser(com.example.streetball_backend.Game.Game game, 
                                com.example.streetball_backend.User.User user);
    
    // 특정 게임에 특정 역할이 이미 존재하는지 확인
    boolean existsByGameGameIdAndRole(Integer gameId, ParticipationRole role);
    
    // 사용자가 참여한 진행 중인 게임 조회 (모집_중, 모집_완료)
    @Query("SELECT p FROM Participation p WHERE p.user.userId = :userId " +
           "AND p.game.status IN (com.example.streetball_backend.Game.GameStatus.모집_중, com.example.streetball_backend.Game.GameStatus.모집_완료)")
    List<Participation> findOngoingGamesByUserId(@Param("userId") Integer userId);
    
    // 사용자가 참여한 과거 게임 조회 (게임_종료)
    @Query("SELECT p FROM Participation p WHERE p.user.userId = :userId " +
           "AND p.game.status = com.example.streetball_backend.Game.GameStatus.게임_종료")
    List<Participation> findPastGamesByUserId(@Param("userId") Integer userId);
}

