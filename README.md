# 🏀 Streetball Backend

> 길거리 농구 매칭 및 관전 시스템 백엔드 API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 목차

- [개요](#-개요)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [데이터베이스 스키마](#-데이터베이스-스키마)
- [프로젝트 구조](#-프로젝트-구조)

---

## 🎯 개요

Streetball Backend는 길거리 농구 게임 매칭을 위한 REST API 서버입니다. 
사용자들이 주변 농구 코트에서 진행되는 게임을 찾고, 참여하고, 평가할 수 있는 기능을 제공합니다.

### 핵심 가치
- **근처 게임 찾기**: GPS 기반으로 주변의 모집 중인 게임 검색
- **역할 기반 참여**: 선수(player), 심판(referee), 관전자(spectator)로 게임 참여
- **평점 시스템**: 게임 종료 후 참여자에 대한 평점 부여

---

## ✨ 주요 기능

### 👤 사용자 (User)
| 기능 | 설명 |
|------|------|
| 회원가입 | 이름, 비밀번호, 공 소유 여부로 계정 생성 |
| 로그인 | JWT 토큰 발급 및 위치 정보 자동 업데이트 |
| 참여 게임 조회 | 진행 중/과거 게임 목록 확인 |

### 🎮 게임 (Game)
| 기능 | 설명 |
|------|------|
| 게임 생성 | 코트, 시간, 최대 인원 설정하여 게임 생성 |
| 근처 게임 검색 | Haversine 공식 기반 반경 내 게임 검색 |
| 게임 참여 | player/referee/spectator 역할로 참여 |
| 게임 취소 | 참여 취소 (모든 참여자 이탈 시 게임 자동 삭제) |
| 자동 상태 변경 | 예정 시간 1시간 후 자동으로 '게임_종료' |
| 시간 충돌 감지 | 같은 코트에서 1시간 내 겹치는 게임 생성 방지 |

### 📍 코트 (Court)
| 기능 | 설명 |
|------|------|
| 코트 목록 조회 | 등록된 모든 농구 코트 조회 |
| 코트별 게임 조회 | 특정 코트에서 진행되는 게임 목록 |

### ⭐ 평점 (Review)
| 기능 | 설명 |
|------|------|
| 평점 생성 | 종료된 게임의 참여자/심판에게 평점 부여 |
| 평점 조회 | 게임별/사용자별 평점 통계 조회 |
| 평점 수정/삭제 | 본인이 작성한 평점 관리 |

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **Framework** | Spring Boot 3.4.0 |
| **Language** | Java 17 |
| **Database** | MySQL 8.x |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | Spring Security + JWT |
| **Build** | Gradle |
| **Documentation** | Swagger/OpenAPI 3.0 |
| **Deploy** | Railway |

---

## 🚀 시작하기

### 사전 요구사항

- Java 17+
- MySQL 8.x
- Gradle 8.x

### 로컬 실행

```bash
# 1. 프로젝트 클론
git clone https://github.com/your-repo/streetball-backend.git
cd streetball-backend/streetball_backend

# 2. 데이터베이스 설정 (application.properties 수정)
# spring.datasource.url=jdbc:mysql://localhost:3306/street-ball
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# 3. 빌드 및 실행
./gradlew clean build
./gradlew bootRun
```

### 프로덕션 빌드

```bash
./gradlew clean build -x test
java -jar build/libs/streetball_backend-0.0.1-SNAPSHOT.jar
```

---

## 📚 API 문서

### Base URL

| 환경 | URL |
|------|-----|
| Local | `http://localhost:8080/api` |
| Production | `https://streetballbackend-production.up.railway.app/api` |

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 주요 엔드포인트

#### 🔐 인증
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/users/signup` | 회원가입 |
| POST | `/api/users/login` | 로그인 (JWT 발급) |

#### 👤 사용자
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/users` | 모든 사용자 조회 |
| GET | `/api/users/{userId}` | 특정 사용자 조회 |
| PUT | `/api/users/{userId}` | 사용자 정보 수정 |
| DELETE | `/api/users/{userId}` | 사용자 삭제 |
| GET | `/api/users/{userId}/games/ongoing` | 진행 중 게임 조회 |
| GET | `/api/users/{userId}/games/past` | 과거 게임 조회 |

#### 🎮 게임
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/games` | 모든 게임 조회 |
| POST | `/api/games` | 게임 생성 |
| GET | `/api/games/{gameId}` | 특정 게임 조회 |
| DELETE | `/api/games/{gameId}` | 게임 삭제 |
| GET | `/api/games/nearby?lat=&lng=&radius=` | 근처 게임 검색 |
| POST | `/api/games/{gameId}/join` | 게임 참여 |
| DELETE | `/api/games/{gameId}/leave?userId=` | 게임 참여 취소 |
| PATCH | `/api/games/{gameId}/status?status=` | 게임 상태 변경 |
| GET | `/api/games/with-referee` | 심판 있는 게임 조회 |
| GET | `/api/games/without-referee` | 심판 없는 게임 조회 |

#### 📍 코트
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/courts` | 모든 코트 조회 |
| GET | `/api/courts/{courtId}` | 특정 코트 조회 |
| GET | `/api/courts/{courtId}/games` | 코트별 게임 조회 |

#### ⭐ 평점
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/reviews` | 평점 생성 (JWT 필요) |
| GET | `/api/reviews/{ratingId}` | 평점 조회 |
| PUT | `/api/reviews/{ratingId}` | 평점 수정 (JWT 필요) |
| DELETE | `/api/reviews/{ratingId}` | 평점 삭제 (JWT 필요) |
| GET | `/api/reviews/game/{gameId}` | 게임별 평점 조회 |
| GET | `/api/reviews/user/{userId}/summary` | 사용자 평점 요약 |
| GET | `/api/reviews/my-reviews/game/{gameId}` | 내가 남긴 평점 조회 (JWT 필요) |

---

## 💾 데이터베이스 스키마

### ERD

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│     User     │       │     Game     │       │    Court     │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ user_id (PK) │◄──┐   │ game_id (PK) │       │ court_id(PK) │
│ name         │   │   │ court_id(FK) │───────┤ court_name   │
│ password     │   │   │ referee_id   │───┐   │ location_lat │
│ location_lat │   │   │ max_players  │   │   │ location_lng │
│ location_lng │   │   │ current_plyr │   │   │ is_indoor    │
│ has_ball     │   │   │ status       │   │   └──────────────┘
│ created_at   │   │   │ scheduled_tm │   │
└──────────────┘   │   │ created_at   │   │
        ▲          │   └──────────────┘   │
        │          │          ▲           │
        │          │          │           │
        │   ┌──────┴──────────┴───────────┘
        │   │
        │   │   ┌────────────────┐
        │   └───┤ Participation  │
        │       ├────────────────┤
        └───────┤ user_id (FK)   │
                │ game_id (FK)   │
                │ role           │
                │ joined_at      │
                └────────────────┘

┌──────────────┐       ┌──────────────┐
│  GameRating  │       │    Review    │
├──────────────┤       ├──────────────┤
│ rating_id    │       │ user_id (PK) │
│ game_id (FK) │       │ play_score   │
│ rater_id(FK) │       │ play_count   │
│ rated_id(FK) │       │ ref_score    │
│ score        │       │ ref_count    │
│ role         │       └──────────────┘
│ created_at   │
└──────────────┘
```

### Enum 타입

```java
// 게임 상태
enum GameStatus { 모집_중, 모집_완료, 게임_종료 }

// 참여 역할
enum ParticipationRole { player, referee, spectator }

// 평점 역할
enum RatingRole { player, referee }
```

---

## 📁 프로젝트 구조

```
streetball_backend/
├── src/main/java/com/example/streetball_backend/
│   ├── config/
│   │   └── JwtUtil.java           # JWT 유틸리티
│   │
│   ├── User/
│   │   ├── User.java              # Entity
│   │   ├── UserController.java    # REST Controller
│   │   ├── UserService.java       # Business Logic
│   │   ├── UserRepository.java    # Data Access
│   │   ├── SignupRequest.java     # DTO
│   │   ├── LoginRequest.java      # DTO
│   │   ├── LoginResponse.java     # DTO
│   │   └── UserResponse.java      # DTO
│   │
│   ├── Game/
│   │   ├── Game.java              # Entity
│   │   ├── GameStatus.java        # Enum
│   │   ├── GameController.java    # REST Controller
│   │   ├── GameService.java       # Business Logic
│   │   ├── GameRepository.java    # Data Access
│   │   ├── GameCreationRequest.java
│   │   ├── JoinGameRequest.java
│   │   ├── NearbyGameRequest.java
│   │   ├── GameResponse.java
│   │   ├── ErrorResponse.java
│   │   └── exception/             # Custom Exceptions
│   │
│   ├── Court/
│   │   ├── Court.java
│   │   ├── CourtController.java
│   │   ├── CourtService.java
│   │   ├── CourtRepository.java
│   │   └── CourtResponse.java
│   │
│   ├── Participation/
│   │   ├── Participation.java
│   │   ├── ParticipationRole.java
│   │   └── ParticipationRepository.java
│   │
│   ├── Review/
│   │   ├── Review.java
│   │   ├── GameRating.java
│   │   ├── RatingRole.java
│   │   ├── ReviewController.java
│   │   ├── ReviewService.java
│   │   ├── ReviewRepository.java
│   │   ├── GameRatingRepository.java
│   │   ├── CreateReviewRequest.java
│   │   ├── UpdateReviewRequest.java
│   │   ├── GameRatingResponse.java
│   │   ├── UserReviewSummaryResponse.java
│   │   └── exception/
│   │
│   ├── SecurityConfig.java        # Spring Security 설정
│   ├── SwaggerConfig.java         # API 문서 설정
│   └── StreetballBackendApplication.java
│
├── src/main/resources/
│   └── application.properties     # 애플리케이션 설정
│
├── build.gradle                   # 의존성 관리
└── settings.gradle
```

---

## 📖 사용 예시

### 1. 회원가입 → 로그인 → 게임 생성

```bash
# 회원가입
curl -X POST https://streetballbackend-production.up.railway.app/api/users/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "농구왕",
    "password": "pass1234",
    "hasBall": true
  }'

# 로그인
curl -X POST https://streetballbackend-production.up.railway.app/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "name": "농구왕",
    "password": "pass1234",
    "locationLat": 37.5665,
    "locationLng": 126.9780
  }'

# 게임 생성
curl -X POST https://streetballbackend-production.up.railway.app/api/games \
  -H "Content-Type: application/json" \
  -d '{
    "courtId": 1,
    "creatorUserId": 1,
    "maxPlayers": 10,
    "scheduledTime": "2025-12-05T14:00:00"
  }'
```

### 2. 근처 게임 검색 → 게임 참여

```bash
# 근처 5km 내 게임 검색
curl "https://streetballbackend-production.up.railway.app/api/games/nearby?lat=37.5665&lng=126.9780&radius=5"

# 게임 참여 (player로)
curl -X POST https://streetballbackend-production.up.railway.app/api/games/1/join \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "role": "player"
  }'
```

---

## 📜 라이선스

MIT License

---

## 👥 개발팀

Street Basketball Development Team

---

**마지막 업데이트**: 2025-12-04
