# Street Basketball API 명세서

## 📋 목차
- [개요](#개요)
- [공통 사항](#공통-사항)
- [User API](#user-api)
- [Game API](#game-api)
- [에러 코드](#에러-코드)

---

## 개요

### Base URL
```
http://localhost:8080/api
```

### 인증
현재 버전에서는 인증이 필요하지 않습니다. (개발 단계)

### Response Format
모든 API는 JSON 형식으로 응답합니다.

---

## 공통 사항

### HTTP Status Codes

| Status Code | 의미 | 설명 |
|------------|------|------|
| 200 | OK | 요청 성공 |
| 201 | Created | 리소스 생성 성공 |
| 204 | No Content | 요청 성공, 응답 본문 없음 |
| 400 | Bad Request | 잘못된 요청 |
| 404 | Not Found | 리소스를 찾을 수 없음 |
| 500 | Internal Server Error | 서버 오류 |

### 공통 데이터 타입

#### Enum: GameStatus
- `모집_중`: 게임 참여자 모집 중
- `모집_완료`: 최대 인원 도달
- `게임_종료`: 게임 종료됨

#### Enum: ParticipationRole
- `player`: 플레이어
- `spectator`: 관전자

---

## User API

### 1. 모든 사용자 조회

사용자 목록을 조회합니다.

**Endpoint**
```
GET /api/users
```

**Request**
- 파라미터 없음

**Response**
```json
[
  {
    "userId": 1,
    "name": "김철수",
    "locationLat": 37.5665,
    "locationLng": 126.9780,
    "hasBall": true,
    "createdAt": "2025-11-24T23:12:22.996392"
  }
]
```

**Response Fields**

| Field | Type | 설명 |
|-------|------|------|
| userId | Integer | 사용자 ID (Primary Key) |
| name | String | 사용자 이름 |
| locationLat | Double | 위도 |
| locationLng | Double | 경도 |
| hasBall | Boolean | 농구공 소유 여부 |
| createdAt | DateTime | 생성 일시 |

**Example**
```bash
curl http://localhost:8080/api/users
```

---

### 2. 사용자 ID로 조회

특정 사용자의 정보를 조회합니다.

**Endpoint**
```
GET /api/users/{userId}
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| userId | Integer | Yes | 사용자 ID |

**Response**
```json
{
  "userId": 1,
  "name": "김철수",
  "locationLat": 37.5665,
  "locationLng": 126.9780,
  "hasBall": true,
  "createdAt": "2025-11-24T23:12:22.996392"
}
```

**Error Response**
- `404 Not Found`: 사용자가 존재하지 않음

**Example**
```bash
curl http://localhost:8080/api/users/1
```

---

### 3. 사용자 생성

새로운 사용자를 생성합니다.

**Endpoint**
```
POST /api/users
```

**Request Headers**
```
Content-Type: application/json
```

**Request Body**
```json
{
  "name": "김철수",
  "locationLat": 37.5665,
  "locationLng": 126.9780,
  "hasBall": true
}
```

**Request Fields**

| Field | Type | Required | 설명 |
|-------|------|----------|------|
| name | String | Yes | 사용자 이름 (최대 100자) |
| locationLat | Double | No | 위도 |
| locationLng | Double | No | 경도 |
| hasBall | Boolean | No | 농구공 소유 여부 (기본값: false) |

**Response** (201 Created)
```json
{
  "userId": 1,
  "name": "김철수",
  "locationLat": 37.5665,
  "locationLng": 126.9780,
  "hasBall": true,
  "createdAt": "2025-11-24T23:12:22.996392"
}
```

**Example**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "김철수",
    "locationLat": 37.5665,
    "locationLng": 126.9780,
    "hasBall": true
  }'
```

---

### 4. 사용자 위치 업데이트 ⭐ 핵심 기능

사용자의 현재 위치를 업데이트합니다.

**Endpoint**
```
PATCH /api/users/{userId}/location
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| userId | Integer | Yes | 사용자 ID |

**Request Headers**
```
Content-Type: application/json
```

**Request Body**
```json
{
  "locationLat": 37.5670,
  "locationLng": 126.9790
}
```

**Request Fields**

| Field | Type | Required | 설명 |
|-------|------|----------|------|
| locationLat | Double | Yes | 새 위도 |
| locationLng | Double | Yes | 새 경도 |

**Response** (200 OK)
```json
{
  "userId": 1,
  "name": "김철수",
  "locationLat": 37.5670,
  "locationLng": 126.9790,
  "hasBall": true,
  "createdAt": "2025-11-24T23:12:22.996392"
}
```

**Error Response**
- `404 Not Found`: 사용자가 존재하지 않음

**Example**
```bash
curl -X PATCH http://localhost:8080/api/users/1/location \
  -H "Content-Type: application/json" \
  -d '{
    "locationLat": 37.5670,
    "locationLng": 126.9790
  }'
```

---

### 5. 사용자 정보 업데이트

사용자의 전체 정보를 업데이트합니다.

**Endpoint**
```
PUT /api/users/{userId}
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| userId | Integer | Yes | 사용자 ID |

**Request Headers**
```
Content-Type: application/json
```

**Request Body**
```json
{
  "name": "김철수(수정)",
  "locationLat": 37.5675,
  "locationLng": 126.9795,
  "hasBall": false
}
```

**Request Fields**

| Field | Type | Required | 설명 |
|-------|------|----------|------|
| name | String | No | 사용자 이름 |
| locationLat | Double | No | 위도 |
| locationLng | Double | No | 경도 |
| hasBall | Boolean | No | 농구공 소유 여부 |

**Response** (200 OK)
```json
{
  "userId": 1,
  "name": "김철수(수정)",
  "locationLat": 37.5675,
  "locationLng": 126.9795,
  "hasBall": false,
  "createdAt": "2025-11-24T23:12:22.996392"
}
```

**Error Response**
- `404 Not Found`: 사용자가 존재하지 않음

**Example**
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "김철수(수정)",
    "locationLat": 37.5675,
    "locationLng": 126.9795,
    "hasBall": false
  }'
```

---

### 6. 사용자 삭제

사용자를 삭제합니다.

**Endpoint**
```
DELETE /api/users/{userId}
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| userId | Integer | Yes | 사용자 ID |

**Response** (204 No Content)
- 응답 본문 없음

**Error Response**
- `404 Not Found`: 사용자가 존재하지 않음

**Example**
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

---

## Game API

### 1. 모든 게임 조회

게임 목록을 조회합니다.

**Endpoint**
```
GET /api/games
```

**Request**
- 파라미터 없음

**Response**
```json
[
  {
    "gameId": 1,
    "courtId": 1,
    "courtName": "강남 농구장",
    "maxPlayers": 10,
    "currentPlayers": 1,
    "status": "모집_중",
    "scheduledTime": "2025-11-25T14:00:00",
    "createdAt": "2025-11-24T23:15:00",
    "refereeId": null
  }
]
```

**Response Fields**

| Field | Type | 설명 |
|-------|------|------|
| gameId | Integer | 게임 ID (Primary Key) |
| courtId | Integer | 코트 ID |
| courtName | String | 코트 이름 |
| maxPlayers | Integer | 최대 참여 인원 |
| currentPlayers | Integer | 현재 참여 인원 |
| status | GameStatus | 게임 상태 (모집_중/모집_완료/게임_종료) |
| scheduledTime | DateTime | 게임 예정 시간 |
| createdAt | DateTime | 생성 일시 |
| refereeId | Integer | 심판 ID (nullable) |

**Example**
```bash
curl http://localhost:8080/api/games
```

---

### 2. 게임 ID로 조회

특정 게임의 정보를 조회합니다.

**Endpoint**
```
GET /api/games/{gameId}
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| gameId | Integer | Yes | 게임 ID |

**Response**
```json
{
  "gameId": 1,
  "courtId": 1,
  "courtName": "강남 농구장",
  "maxPlayers": 10,
  "currentPlayers": 1,
  "status": "모집_중",
  "scheduledTime": "2025-11-25T14:00:00",
  "createdAt": "2025-11-24T23:15:00",
  "refereeId": null
}
```

**Error Response**
- `404 Not Found`: 게임이 존재하지 않음

**Example**
```bash
curl http://localhost:8080/api/games/1
```

---

### 3. 게임 생성 ⭐ 핵심 기능

새로운 게임을 생성합니다. 생성자는 자동으로 'player' 역할로 게임에 참여됩니다.

**Endpoint**
```
POST /api/games
```

**Request Headers**
```
Content-Type: application/json
```

**Request Body**
```json
{
  "courtId": 1,
  "creatorUserId": 1,
  "maxPlayers": 10,
  "scheduledTime": "2025-11-25T14:00:00"
}
```

**Request Fields**

| Field | Type | Required | 설명 |
|-------|------|----------|------|
| courtId | Integer | Yes | 코트 ID (Court 테이블에 존재해야 함) |
| creatorUserId | Integer | Yes | 생성자 사용자 ID |
| maxPlayers | Integer | No | 최대 참여 인원 (기본값: 10) |
| scheduledTime | DateTime | Yes | 게임 예정 시간 (ISO 8601 형식) |

**Response** (201 Created)
```json
{
  "gameId": 1,
  "courtId": 1,
  "courtName": "강남 농구장",
  "maxPlayers": 10,
  "currentPlayers": 1,
  "status": "모집_중",
  "scheduledTime": "2025-11-25T14:00:00",
  "createdAt": "2025-11-24T23:15:00",
  "refereeId": null
}
```

**비즈니스 로직**
1. 게임 생성
2. 생성자를 Participation 테이블에 'player' 역할로 자동 등록
3. currentPlayers가 1로 시작
4. 트랜잭션으로 원자성 보장

**Error Response**
- `400 Bad Request`: 잘못된 요청 (courtId 또는 creatorUserId가 존재하지 않음)

**Example**
```bash
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{
    "courtId": 1,
    "creatorUserId": 1,
    "maxPlayers": 10,
    "scheduledTime": "2025-11-25T14:00:00"
  }'
```

---

### 4. 근처 게임 검색 ⭐ 핵심 기능

현재 위치에서 지정된 반경 내의 '모집 중' 게임을 검색합니다.

**Endpoint**
```
GET /api/games/nearby
```

**Query Parameters**

| Parameter | Type | Required | Default | 설명 |
|-----------|------|----------|---------|------|
| lat | Double | Yes | - | 현재 위치의 위도 |
| lng | Double | Yes | - | 현재 위치의 경도 |
| radius | Integer | No | 5 | 검색 반경 (km) |

**Response**
```json
[
  {
    "gameId": 1,
    "courtId": 1,
    "courtName": "강남 농구장",
    "maxPlayers": 10,
    "currentPlayers": 5,
    "status": "모집_중",
    "scheduledTime": "2025-11-25T14:00:00",
    "createdAt": "2025-11-24T23:15:00",
    "refereeId": null
  },
  {
    "gameId": 2,
    "courtId": 3,
    "courtName": "홍대 스트리트 코트",
    "maxPlayers": 8,
    "currentPlayers": 3,
    "status": "모집_중",
    "scheduledTime": "2025-11-25T16:00:00",
    "createdAt": "2025-11-24T23:20:00",
    "refereeId": null
  }
]
```

**비즈니스 로직**
1. Bounding Box를 사용한 1차 필터링
2. Haversine 공식으로 실제 거리 계산
3. 반경 내의 코트에서 진행되는 '모집_중' 상태의 게임만 반환

**Example**
```bash
# 강남 근처 5km 반경 검색
curl "http://localhost:8080/api/games/nearby?lat=37.5665&lng=126.9780&radius=5"

# 잠실 근처 10km 반경 검색
curl "http://localhost:8080/api/games/nearby?lat=37.5145&lng=127.1025&radius=10"
```

---

### 5. 게임 상태 업데이트

게임의 상태를 변경합니다.

**Endpoint**
```
PATCH /api/games/{gameId}/status
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| gameId | Integer | Yes | 게임 ID |

**Query Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| status | GameStatus | Yes | 새 게임 상태 (모집_중/모집_완료/게임_종료) |

**Response** (200 OK)
```json
{
  "gameId": 1,
  "courtId": 1,
  "courtName": "강남 농구장",
  "maxPlayers": 10,
  "currentPlayers": 10,
  "status": "모집_완료",
  "scheduledTime": "2025-11-25T14:00:00",
  "createdAt": "2025-11-24T23:15:00",
  "refereeId": null
}
```

**Error Response**
- `404 Not Found`: 게임이 존재하지 않음

**Example**
```bash
# 모집 완료로 변경
curl -X PATCH "http://localhost:8080/api/games/1/status?status=모집_완료"

# 게임 종료로 변경
curl -X PATCH "http://localhost:8080/api/games/1/status?status=게임_종료"
```

---

### 6. 특정 상태의 게임 조회

특정 상태의 게임 목록을 조회합니다.

**Endpoint**
```
GET /api/games/status/{status}
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| status | GameStatus | Yes | 게임 상태 (모집_중/모집_완료/게임_종료) |

**Response**
```json
[
  {
    "gameId": 1,
    "courtId": 1,
    "courtName": "강남 농구장",
    "maxPlayers": 10,
    "currentPlayers": 5,
    "status": "모집_중",
    "scheduledTime": "2025-11-25T14:00:00",
    "createdAt": "2025-11-24T23:15:00",
    "refereeId": null
  }
]
```

**Example**
```bash
# 모집 중인 게임 조회
curl http://localhost:8080/api/games/status/모집_중

# 모집 완료된 게임 조회
curl http://localhost:8080/api/games/status/모집_완료

# 종료된 게임 조회
curl http://localhost:8080/api/games/status/게임_종료
```

---

### 7. 게임 삭제

게임을 삭제합니다.

**Endpoint**
```
DELETE /api/games/{gameId}
```

**Path Parameters**

| Parameter | Type | Required | 설명 |
|-----------|------|----------|------|
| gameId | Integer | Yes | 게임 ID |

**Response** (204 No Content)
- 응답 본문 없음

**Error Response**
- `404 Not Found`: 게임이 존재하지 않음

**Example**
```bash
curl -X DELETE http://localhost:8080/api/games/1
```

---

## 에러 코드

### 공통 에러 응답 형식

```json
{
  "timestamp": "2025-11-24T23:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "사용자를 찾을 수 없습니다. ID: 999",
  "path": "/api/users/999"
}
```

### 주요 에러 메시지

| HTTP Status | 메시지 | 설명 |
|------------|--------|------|
| 404 | 사용자를 찾을 수 없습니다. ID: {userId} | 해당 ID의 사용자가 없음 |
| 404 | 게임을 찾을 수 없습니다. ID: {gameId} | 해당 ID의 게임이 없음 |
| 400 | 코트를 찾을 수 없습니다. ID: {courtId} | 해당 ID의 코트가 없음 |
| 400 | 잘못된 요청 | 필수 필드 누락 또는 형식 오류 |

---

## 사용 예시

### 시나리오 1: 사용자 등록 및 근처 게임 검색

```bash
# 1. 사용자 생성
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "김철수",
    "locationLat": 37.5665,
    "locationLng": 126.9780,
    "hasBall": true
  }'

# 2. 근처 게임 검색
curl "http://localhost:8080/api/games/nearby?lat=37.5665&lng=126.9780&radius=5"
```

### 시나리오 2: 게임 생성 및 참여

```bash
# 1. 사용자 생성
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "이영희", "locationLat": 37.5665, "locationLng": 126.9780, "hasBall": false}'

# 2. 게임 생성 (생성자 자동 참여)
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{
    "courtId": 1,
    "creatorUserId": 1,
    "maxPlayers": 10,
    "scheduledTime": "2025-11-25T14:00:00"
  }'

# 3. 게임 조회
curl http://localhost:8080/api/games/1
```

### 시나리오 3: 위치 업데이트 후 근처 게임 재검색

```bash
# 1. 사용자 위치 업데이트
curl -X PATCH http://localhost:8080/api/users/1/location \
  -H "Content-Type: application/json" \
  -d '{"locationLat": 37.5145, "locationLng": 127.1025}'

# 2. 새 위치 기준으로 근처 게임 검색
curl "http://localhost:8080/api/games/nearby?lat=37.5145&lng=127.1025&radius=10"
```

---

## 부록

### DateTime 형식
ISO 8601 형식을 사용합니다:
```
2025-11-25T14:00:00
```

### 좌표계
- WGS84 좌표계 사용
- 위도(latitude): -90 ~ 90
- 경도(longitude): -180 ~ 180

### 거리 계산
Haversine 공식을 사용하여 두 지점 간의 대권 거리(Great-circle distance)를 계산합니다.

---

**최종 업데이트**: 2025-11-24  
**API 버전**: 1.0.0  
**문의**: Street Basketball Development Team

