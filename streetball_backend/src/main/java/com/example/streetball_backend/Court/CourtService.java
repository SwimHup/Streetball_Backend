package com.example.streetball_backend.Court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.streetball_backend.Game.GameResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    public List<CourtResponse> getAllCourts() {
        return courtRepository.findAll().stream()
                .map(CourtResponse::new)
                .collect(Collectors.toList());
    }

    public CourtResponse getCourtById(Integer courtId) {
        return courtRepository.findByCourtId(courtId)
                .map(CourtResponse::new)
                .orElseThrow(() -> new RuntimeException("코트를 찾을 수 없습니다. ID: " + courtId));
    }

    public List<GameResponse> getGamesByCourtId(Integer courtId) {
        return courtRepository.findGamesByCourtId(courtId).stream()
                .map(GameResponse::new)
                .collect(Collectors.toList());
    }
}
