package com.example.streetball_backend.Court;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer> {
    
    // 위치 기반 검색을 위한 메서드 (실제 거리 계산은 서비스 레이어에서 처리)
    @Query("SELECT c FROM Court c WHERE " +
           "c.locationLat BETWEEN :minLat AND :maxLat AND " +
           "c.locationLng BETWEEN :minLng AND :maxLng")
    List<Court> findCourtsInBounds(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng);
}

