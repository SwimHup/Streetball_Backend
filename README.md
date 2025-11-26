# 길거리 농구 매칭 및 관전 시스템 (Street Basketball Matching and Spectating System)

Spring Boot 기반의 길거리 농구 매칭 및 관전 시스템 백엔드 API입니다.

## 프로젝트 구조

```
streetball_backend/
├── src/main/java/com/example/streetball_backend/
│   ├── Court/              # 코트 관련
│   │   ├── Court.java      # Entity
│   │   └── CourtRepository.java
│   ├── Game/               # 게임 관련
│   │   ├── Game.java       # Entity
│   │   ├── GameStatus.java # Enum
│   │   ├── GameRepository.java
│   │   ├── GameService.java
│   │   ├── GameController.java
│   │   ├── GameCreationRequest.java
│   │   ├── GameResponse.java
│   │   └── NearbyGameRequest.java
│   ├── Participation/      # 참여 관련
│   │   ├── Participation.java
│   │   ├── ParticipationRole.java
│   │   └── ParticipationRepository.java
│   ├── Review/             # 리뷰 관련
│   │   ├── Review.java
│   │   └── ReviewRepository.java
│   ├── User/               # 사용자 관련
│   │   ├── User.java
│   │   ├── UserRepository.java
│   │   ├── UserService.java
│   │   ├── UserController.java
│   │   ├── UserCreateRequest.java
│   │   ├── UserLocationRequest.java
│   │   └── UserResponse.java
│   ├── SecurityConfig.java
│   └── StreetballBackendApplication.java
└── src/main/resources/
    ├── application.yml     # DB 설정
    └── application.properties
```

## 데이터베이스 스키마

### 주요 테이블

| 테이블 | 주요 필드 |
|--------|-----------|
| **User** | `user_id` (PK), `name` (unique), `password`, `location_lat`, `location_lng`, `has_ball`, `created_at` |
| **Court** | `court_id` (PK), `court_name`, `location_lat`, `location_lng`, `is_indoor` |
| **Game** | `game_id` (PK), `court_id` (FK), `max_players`, `current_players`, `status`, `scheduled_time` |
| **Participation** | `participation_id` (PK), `game_id` (FK), `user_id` (FK), `role` (ENUM: player/referee(최대1명)/spectator) |
| **Review** | `user_id` (PK, FK), `play_score`, `play_count`, `ref_score`, `ref_count` |

## API 엔드포인트

### User API

#### 1. 모든 사용자 조회
```
GET /api/users
```

#### 2. 사용자 ID로 조회
```
GET /api/users/{userId}
```

#### 3. 회원가입 (핵심 기능)
```
POST /api/users/signup
Content-Type: application/json

{
  "name": "홍길동",
  "password": "password123",
  "hasBall": true
}
```

#### 4. 로그인 (핵심 기능)
```
POST /api/users/login
Content-Type: application/json

{
  "name": "홍길동",
  "password": "password123",
  "locationLat": 37.5665,
  "locationLng": 126.9780
}
```
**응답:** JWT 토큰과 사용자 정보 반환 (토큰은 이후 API 호출 시 `Authorization: Bearer {token}` 헤더로 사용)
**참고:** 로그인 시 위치 정보가 자동으로 업데이트됩니다.

#### 5. 사용자 정보 업데이트
```
PUT /api/users/{userId}
Content-Type: application/json

{
  "name": "홍길동",
  "locationLat": 37.5665,
  "locationLng": 126.9780,
  "hasBall": true
}
```

#### 6. 사용자 삭제
```
DELETE /api/users/{userId}
```

### Game API

#### 1. 모든 게임 조회
```
GET /api/games
```

#### 2. 게임 ID로 조회
```
GET /api/games/{gameId}
```

#### 3. 새 게임 생성 (핵심 기능)
```
POST /api/games
Content-Type: application/json

{
  "courtId": 1,
  "creatorUserId": 1,
  "maxPlayers": 10,
  "scheduledTime": "2025-11-25T14:00:00"
}
```
**참고:** 게임 생성 시 생성자가 자동으로 'player' 역할로 Participation 테이블에 등록됩니다.

#### 4. 근처 게임 검색 (핵심 기능)
```
GET /api/games/nearby?lat=37.5665&lng=126.9780&radius=5
```
- `lat`: 현재 위도
- `lng`: 현재 경도
- `radius`: 검색 반경 (km, 기본값: 5)

**기능:** Haversine 공식을 사용하여 실제 거리를 계산하고, 반경 내의 코트에서 진행되는 '모집 중' 상태의 게임을 반환합니다.

#### 5. 게임 상태 업데이트
```
PATCH /api/games/{gameId}/status?status=모집_완료
```
- 가능한 상태: `모집_중`, `모집_완료`, `게임_종료`

#### 6. 특정 상태의 게임 조회
```
GET /api/games/status/{status}
```

#### 7. 게임 삭제
```
DELETE /api/games/{gameId}
```

## 핵심 비즈니스 로직

### 1. 게임 생성 (GameService.createGame)
- 새 게임 생성 시 생성자를 자동으로 Participation 테이블에 'player' 역할로 등록
- Transaction을 사용하여 Game과 Participation이 동시에 생성되도록 보장
- current_players 자동 증가 및 상태 업데이트

### 2. 근처 게임 검색 (GameService.findNearbyGames)
- Bounding Box를 사용한 1차 필터링
- Haversine 공식을 사용한 정확한 거리 계산
- '모집 중' 상태의 게임만 반환

### 3. 사용자 위치 저장 (UserService.updateUserLocation)
- 사용자의 현재 위치(위도, 경도)를 업데이트
- 근처 게임 검색의 기준이 되는 정보



## 빌드 및 실행

### 빌드
```bash
cd streetball_backend
./gradlew clean build
```

### 실행
```bash
./gradlew bootRun
```

또는 빌드된 JAR 파일 실행:
```bash
java -jar build/libs/streetball_backend-0.0.1-SNAPSHOT.jar
```

## 기술 스택

- **Framework:** Spring Boot 4.0.0
- **Language:** Java 17
- **Database:** MySQL 8.x
- **ORM:** JPA/Hibernate
- **Build Tool:** Gradle
- **Security:** Spring Security (개발 단계에서 비활성화)

## 주요 의존성

- `spring-boot-starter-data-jpa`: JPA 지원
- `spring-boot-starter-webmvc`: REST API 지원
- `mysql-connector-j`: MySQL 드라이버
- `spring-boot-starter-security`: 보안 (현재 모든 엔드포인트 허용)

## 개발 참고사항

### Enum 타입
- `GameStatus`: `모집_중`, `모집_완료`, `게임_종료`
- `ParticipationRole`: `player`, `spectator`

### 트리거
데이터베이스에는 다음 트리거가 설정되어 있습니다:
- `trg_after_insert_participation`: 참여자 추가 시 current_players 증가
- `trg_after_delete_participation`: 참여자 삭제 시 current_players 감소

### 제약조건
- Game-User 간 Participation은 UNIQUE (한 게임에 한 사용자는 한 번만 참여 가능)
- Game의 referee_id는 UNIQUE (한 사용자는 한 게임만 심판 가능)
- Review는 User와 1:1 관계

## 향후 개선 사항

- [ ] 실시간 위치 추적 (WebSocket)
- [ ] 리뷰 시스템 API 구현
- [ ] 참여자 관리 API 구현
- [ ] 인증/인가 기능 추가
- [ ] Swagger/OpenAPI 문서 자동 생성
- [ ] 테스트 코드 작성
- [ ] 에러 핸들링 개선 (Global Exception Handler)
- [ ] 페이지네이션 추가
