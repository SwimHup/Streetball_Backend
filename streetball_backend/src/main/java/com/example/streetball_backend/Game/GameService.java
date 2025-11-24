package com.example.streetball_backend.Game;

import com.example.streetball_backend.Court.Court;
import com.example.streetball_backend.Court.CourtRepository;
import com.example.streetball_backend.Participation.Participation;
import com.example.streetball_backend.Participation.ParticipationRepository;
import com.example.streetball_backend.Participation.ParticipationRole;
import com.example.streetball_backend.User.User;
import com.example.streetball_backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    /**
     * 모든 게임 조회
     */
    public List<GameResponse> getAllGames() {
        return gameRepository.findAll().stream()
                .map(GameResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * ID로 게임 조회
     */
    public GameResponse getGameById(Integer gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("게임을 찾을 수 없습니다. ID: " + gameId));
        return new GameResponse(game);
    }

    /**
     * 새 게임 생성 (핵심 기능)
     * 생성 후 생성자를 Participation 테이블에 'player' 역할로 자동 등록
     */
    @Transactional
    public GameResponse createGame(GameCreationRequest request) {
        // Court 조회
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new RuntimeException("코트를 찾을 수 없습니다. ID: " + request.getCourtId()));

        // User 조회
        User creator = userRepository.findById(request.getCreatorUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + request.getCreatorUserId()));

        // Game 생성
        Game game = new Game(
                court,
                request.getMaxPlayers() != null ? request.getMaxPlayers() : 10,
                request.getScheduledTime()
        );
        Game savedGame = gameRepository.save(game);

        // 생성자를 Participation 테이블에 'player' 역할로 자동 등록
        Participation participation = new Participation(savedGame, creator, ParticipationRole.player);
        participationRepository.save(participation);

        // DB 트리거가 current_players를 업데이트하지만, 현재 세션에서는 반영되지 않으므로
        // 명시적으로 다시 조회하거나 수동으로 업데이트
        savedGame.setCurrentPlayers(savedGame.getCurrentPlayers() + 1);
        if (savedGame.getCurrentPlayers() >= savedGame.getMaxPlayers()) {
            savedGame.setStatus(GameStatus.모집_완료);
        }
        Game updatedGame = gameRepository.save(savedGame);

        return new GameResponse(updatedGame);
    }

    /**
     * 근처 게임 검색 (핵심 기능)
     * 현재 위치에서 지정된 반경 내의 코트를 찾고, 해당 코트에서 진행될 Game 목록 반환
     */
    public List<GameResponse> findNearbyGames(double lat, double lng, int radiusKm) {
        // 1. 반경 내의 코트 찾기 (간단한 Bounding Box 계산)
        // 위도 1도 ≈ 111km, 경도 1도 ≈ 88.8km (중위도 기준)
        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / 88.8;

        double minLat = lat - latDelta;
        double maxLat = lat + latDelta;
        double minLng = lng - lngDelta;
        double maxLng = lng + lngDelta;

        // Bounding Box 내의 코트 조회
        List<Court> nearbyCourts = courtRepository.findCourtsInBounds(minLat, maxLat, minLng, maxLng);

        // 2. 실제 거리 계산 및 필터링 (Haversine 공식 사용)
        List<Court> filteredCourts = nearbyCourts.stream()
                .filter(court -> {
                    double distance = calculateDistance(lat, lng, court.getLocationLat(), court.getLocationLng());
                    return distance <= radiusKm;
                })
                .collect(Collectors.toList());

        // 3. 해당 코트들에서 진행될 '모집 중' 게임 조회
        if (filteredCourts.isEmpty()) {
            return List.of();
        }

        List<Integer> courtIds = filteredCourts.stream()
                .map(Court::getCourtId)
                .collect(Collectors.toList());

        List<Game> nearbyGames = gameRepository.findByCourtsAndStatus(courtIds, GameStatus.모집_중);

        return nearbyGames.stream()
                .map(GameResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Haversine 공식을 사용한 두 지점 간 거리 계산 (km 단위)
     */
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS = 6371; // 지구 반지름 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * 게임 상태 업데이트
     */
    @Transactional
    public GameResponse updateGameStatus(Integer gameId, GameStatus status) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("게임을 찾을 수 없습니다. ID: " + gameId));
        
        game.setStatus(status);
        Game updatedGame = gameRepository.save(game);
        return new GameResponse(updatedGame);
    }

    /**
     * 게임 삭제
     */
    @Transactional
    public void deleteGame(Integer gameId) {
        if (!gameRepository.existsById(gameId)) {
            throw new RuntimeException("게임을 찾을 수 없습니다. ID: " + gameId);
        }
        gameRepository.deleteById(gameId);
    }

    /**
     * 특정 상태의 게임 목록 조회
     */
    public List<GameResponse> getGamesByStatus(GameStatus status) {
        return gameRepository.findByStatus(status).stream()
                .map(GameResponse::new)
                .collect(Collectors.toList());
    }
}

