package com.example.streetball_backend.User;

import com.example.streetball_backend.Game.GameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "모든 사용자 조회", description = "시스템에 등록된 모든 사용자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "사용자 ID로 조회", description = "특정 사용자의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Integer userId) {
        try {
            UserResponse user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "회원가입 ⭐", description = "새로운 사용자를 시스템에 등록합니다. 이름, 비밀번호, 공 소유 여부를 입력받습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (이름 중복 등)")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원가입 정보", required = true)
            @RequestBody SignupRequest request) {
        try {
            UserResponse newUser = userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "로그인 ⭐", description = "이름, 비밀번호, 현재 위치로 로그인합니다. 로그인 시 위치 정보가 자동으로 업데이트됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패 (사용자 없음 또는 비밀번호 불일치)")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 정보 (위치 포함)", required = true)
            @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "사용자 정보 업데이트", description = "사용자의 전체 정보를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "업데이트 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Integer userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "업데이트할 사용자 정보", required = true)
            @RequestBody UserCreateRequest request) {
        try {
            UserResponse updatedUser = userService.updateUser(userId, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "사용자 삭제", description = "사용자를 시스템에서 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Integer userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "진행 중인 참여 게임 조회 ⭐", description = "사용자가 참여한 진행 중인 게임 목록을 조회합니다. (모집_중, 모집_완료)")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userId}/games/ongoing")
    public ResponseEntity<List<GameResponse>> getOngoingGames(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Integer userId) {
        try {
            List<GameResponse> games = userService.getOngoingGames(userId);
            return ResponseEntity.ok(games);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "과거 참여 게임 조회", description = "사용자가 참여했던 과거 게임 목록을 조회합니다. (게임_종료)")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userId}/games/past")
    public ResponseEntity<List<GameResponse>> getPastGames(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Integer userId) {
        try {
            List<GameResponse> games = userService.getPastGames(userId);
            return ResponseEntity.ok(games);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "게임 참여 취소 ⭐", description = "사용자가 참여한 게임을 취소합니다. 진행 중인 게임만 취소 가능합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "취소 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (종료된 게임 등)"),
        @ApiResponse(responseCode = "404", description = "참여 내역을 찾을 수 없음")
    })
    @DeleteMapping("/{userId}/games/{gameId}")
    public ResponseEntity<Void> cancelGameParticipation(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @PathVariable Integer userId,
            @Parameter(description = "게임 ID", required = true, example = "1")
            @PathVariable Integer gameId) {
        try {
            userService.cancelGameParticipation(userId, gameId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

