package com.example.streetball_backend.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * 모든 게임 조회
     * GET /api/games
     */
    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames() {
        List<GameResponse> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    /**
     * ID로 게임 조회
     * GET /api/games/{gameId}
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable Integer gameId) {
        try {
            GameResponse game = gameService.getGameById(gameId);
            return ResponseEntity.ok(game);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 새 게임 생성 (핵심 기능)
     * POST /api/games
     */
    @PostMapping
    public ResponseEntity<GameResponse> createGame(@RequestBody GameCreationRequest request) {
        try {
            GameResponse createdGame = gameService.createGame(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 근처 게임 검색 (핵심 기능)
     * GET /api/games/nearby?lat={lat}&lng={lng}&radius={radius}
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<GameResponse>> getNearbyGames(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "5") Integer radius) {
        try {
            List<GameResponse> nearbyGames = gameService.findNearbyGames(lat, lng, radius);
            return ResponseEntity.ok(nearbyGames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 게임 상태 업데이트
     * PATCH /api/games/{gameId}/status
     */
    @PatchMapping("/{gameId}/status")
    public ResponseEntity<GameResponse> updateGameStatus(
            @PathVariable Integer gameId,
            @RequestParam GameStatus status) {
        try {
            GameResponse updatedGame = gameService.updateGameStatus(gameId, status);
            return ResponseEntity.ok(updatedGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 게임 삭제
     * DELETE /api/games/{gameId}
     */
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Integer gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 특정 상태의 게임 목록 조회
     * GET /api/games/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GameResponse>> getGamesByStatus(@PathVariable GameStatus status) {
        List<GameResponse> games = gameService.getGamesByStatus(status);
        return ResponseEntity.ok(games);
    }
}

