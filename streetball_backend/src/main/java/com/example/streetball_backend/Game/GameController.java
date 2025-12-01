package com.example.streetball_backend.Game;

import com.example.streetball_backend.Game.exception.AlreadyParticipatingException;
import com.example.streetball_backend.Game.exception.GameFullException;
import com.example.streetball_backend.Game.exception.GameNotRecruitingException;
import com.example.streetball_backend.Game.exception.RefereeAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Game", description = "게임 관리 API")
public class GameController {

    @Autowired
    private GameService gameService;

    @Operation(summary = "모든 게임 조회", description = "시스템에 등록된 모든 게임 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames() {
        List<GameResponse> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "게임 ID로 조회", description = "특정 게임의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGameById(
            @Parameter(description = "게임 ID", required = true, example = "1")
            @PathVariable Integer gameId) {
        try {
            GameResponse game = gameService.getGameById(gameId);
            return ResponseEntity.ok(game);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "새 게임 생성 ⭐", description = "새로운 게임을 생성하고 생성자를 자동으로 'player' 역할로 참여시킵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (courtId 또는 creatorUserId가 존재하지 않음)")
    })
    @PostMapping
    public ResponseEntity<GameResponse> createGame(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "게임 생성 정보", required = true)
            @RequestBody GameCreationRequest request) {
        try {
            GameResponse createdGame = gameService.createGame(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "근처 게임 검색 ⭐", description = "현재 위치에서 지정된 반경 내의 '모집 중' 게임을 검색합니다. Haversine 공식을 사용하여 정확한 거리를 계산합니다.")
    @ApiResponse(responseCode = "200", description = "검색 성공")
    @GetMapping("/nearby?lat={lat}&lng={lng}&radius={radius}")
    public ResponseEntity<List<GameResponse>> getNearbyGames(
            @Parameter(description = "현재 위치의 위도", required = true, example = "37.5665")
            @RequestParam Double lat,
            @Parameter(description = "현재 위치의 경도", required = true, example = "126.9780")
            @RequestParam Double lng,
            @Parameter(description = "검색 반경 (km)", example = "5")
            @RequestParam(defaultValue = "5") Integer radius) {
        try {
            List<GameResponse> nearbyGames = gameService.findNearbyGames(lat, lng, radius);
            return ResponseEntity.ok(nearbyGames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "게임 상태 업데이트", description = "게임의 상태를 변경합니다. (모집_중, 모집_완료, 게임_종료)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
    })
    @PatchMapping("/{gameId}/status")
    public ResponseEntity<GameResponse> updateGameStatus(
            @Parameter(description = "게임 ID", required = true, example = "1")
            @PathVariable Integer gameId,
            @Parameter(description = "새 게임 상태", required = true, example = "모집_완료")
            @RequestParam GameStatus status) {
        try {
            GameResponse updatedGame = gameService.updateGameStatus(gameId, status);
            return ResponseEntity.ok(updatedGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "게임 삭제", description = "게임을 시스템에서 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "게임을 찾을 수 없음")
    })
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(
            @Parameter(description = "게임 ID", required = true, example = "1")
            @PathVariable Integer gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "특정 상태의 게임 목록 조회", description = "특정 상태(모집_중, 모집_완료, 게임_종료)의 게임 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<GameResponse>> getGamesByStatus(
            @Parameter(description = "게임 상태", required = true, example = "모집_중")
            @PathVariable GameStatus status) {
        List<GameResponse> games = gameService.getGamesByStatus(status);
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "게임 참여 ⭐", description = "사용자가 원하는 역할(player, referee, spectator)로 게임에 참여합니다. 선착순으로 참여할 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "참여 성공",
                content = @Content(schema = @Schema(implementation = GameResponse.class))),
        @ApiResponse(responseCode = "400", description = "모집 중인 게임이 아님",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "게임 또는 사용자를 찾을 수 없음",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "이미 참여함 / 최대 인원 초과 / 심판 이미 존재",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{gameId}/join")
    public ResponseEntity<?> joinGame(
            @Parameter(description = "게임 ID", required = true, example = "1")
            @PathVariable Integer gameId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "게임 참여 정보", required = true)
            @RequestBody JoinGameRequest request) {
        try {
            GameResponse updatedGame = gameService.joinGame(gameId, request);
            return ResponseEntity.ok(updatedGame);
        } catch (AlreadyParticipatingException e) {
            // 409 Conflict - 이미 참여함
            ErrorResponse error = new ErrorResponse("ALREADY_PARTICIPATING", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (GameFullException e) {
            // 409 Conflict - 최대 인원 초과
            ErrorResponse error = new ErrorResponse("GAME_FULL", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (RefereeAlreadyExistsException e) {
            // 409 Conflict - 심판 이미 존재
            ErrorResponse error = new ErrorResponse("REFEREE_ALREADY_EXISTS", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (GameNotRecruitingException e) {
            // 400 Bad Request - 모집 중이 아님
            ErrorResponse error = new ErrorResponse("GAME_NOT_RECRUITING", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (RuntimeException e) {
            // 404 Not Found - 게임 또는 사용자를 찾을 수 없음
            ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "게임 참여 취소 ⭐", description = "사용자가 게임 참여를 취소합니다. 참여 취소 후 다시 참여하면 참여자 목록 끝에 추가됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "취소 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (참여 내역 없음 등)")
    })
    @DeleteMapping("/{gameId}/leave")
    public ResponseEntity<GameResponse> leaveGame(
            @Parameter(description = "게임 ID", required = true, example = "1")
            @PathVariable Integer gameId,
            @Parameter(description = "사용자 ID", required = true, example = "5")
            @RequestParam Integer userId) {
        try {
            GameResponse updatedGame = gameService.leaveGame(gameId, userId);
            return ResponseEntity.ok(updatedGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "심판 있는 게임 조회 ⭐", description = "심판이 등록된 모집 중인 게임 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/with-referee")
    public ResponseEntity<List<GameResponse>> getGamesWithReferee() {
        List<GameResponse> games = gameService.getGamesWithReferee();
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "심판 없는 게임 조회 ⭐", description = "심판이 등록되지 않은 모집 중인 게임 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/without-referee")
    public ResponseEntity<List<GameResponse>> getGamesWithoutReferee() {
        List<GameResponse> games = gameService.getGamesWithoutReferee();
        return ResponseEntity.ok(games);
    }
}

