package com.example.streetball_backend.Court;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.streetball_backend.Game.GameResponse;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
@Tag(name = "Court", description = "코트 관리 API")
public class CourtController {

    @Autowired
    private CourtService courtService;

    @Operation(summary = "모든 코트 조회", description = "시스템에 등록된 모든 코트 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<CourtResponse>> getAllCourts() {
        List<CourtResponse> courts = courtService.getAllCourts();
        return ResponseEntity.ok(courts);
    }

    @Operation(summary = "특정 코트 조회", description = "특정 코트의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{courtId}")
    public ResponseEntity<CourtResponse> getCourtById(
            @Parameter(description = "코트 ID", required = true, example = "1")
            @PathVariable Integer courtId) {
        CourtResponse court = courtService.getCourtById(courtId);
        return ResponseEntity.ok(court);
    }

    @Operation(summary = "특정 코트의 게임 조회", description = "특정 코트의 게임 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{courtId}/games")
    public ResponseEntity<List<GameResponse>> getGamesByCourtId(
            @Parameter(description = "코트 ID", required = true, example = "1")
            @PathVariable Integer courtId) {
        List<GameResponse> games = courtService.getGamesByCourtId(courtId);
        return ResponseEntity.ok(games);
    }
}

