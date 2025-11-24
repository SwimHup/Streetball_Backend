#!/bin/bash

# Street Basketball API 테스트 스크립트
# 사용법: ./test-with-curl.sh

BASE_URL="http://localhost:8080/api"

echo "================================"
echo "Street Basketball API 테스트"
echo "================================"
echo ""

# 색상 코드
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 1. 사용자 생성
echo -e "${BLUE}[1] 사용자 생성${NC}"
echo "POST ${BASE_URL}/users"
curl -X POST "${BASE_URL}/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "김철수",
    "locationLat": 37.5665,
    "locationLng": 126.9780,
    "hasBall": true
  }' | jq '.'
echo ""
echo ""

# 2. 두 번째 사용자 생성
echo -e "${BLUE}[2] 두 번째 사용자 생성${NC}"
curl -X POST "${BASE_URL}/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "이영희",
    "locationLat": 37.5660,
    "locationLng": 126.9785,
    "hasBall": false
  }' | jq '.'
echo ""
echo ""

# 3. 모든 사용자 조회
echo -e "${BLUE}[3] 모든 사용자 조회${NC}"
echo "GET ${BASE_URL}/users"
curl -X GET "${BASE_URL}/users" | jq '.'
echo ""
echo ""

# 4. 특정 사용자 조회
echo -e "${BLUE}[4] 특정 사용자 조회 (userId=1)${NC}"
echo "GET ${BASE_URL}/users/1"
curl -X GET "${BASE_URL}/users/1" | jq '.'
echo ""
echo ""

# 5. 사용자 위치 업데이트
echo -e "${BLUE}[5] 사용자 위치 업데이트 (userId=1)${NC}"
echo "PATCH ${BASE_URL}/users/1/location"
curl -X PATCH "${BASE_URL}/users/1/location" \
  -H "Content-Type: application/json" \
  -d '{
    "locationLat": 37.5670,
    "locationLng": 126.9790
  }' | jq '.'
echo ""
echo ""

# 6. 게임 생성 (Court가 DB에 있어야 함)
echo -e "${BLUE}[6] 게임 생성 (courtId=1 필요)${NC}"
echo "POST ${BASE_URL}/games"
curl -X POST "${BASE_URL}/games" \
  -H "Content-Type: application/json" \
  -d '{
    "courtId": 1,
    "creatorUserId": 1,
    "maxPlayers": 10,
    "scheduledTime": "2025-11-25T14:00:00"
  }' | jq '.'
echo ""
echo ""

# 7. 모든 게임 조회
echo -e "${BLUE}[7] 모든 게임 조회${NC}"
echo "GET ${BASE_URL}/games"
curl -X GET "${BASE_URL}/games" | jq '.'
echo ""
echo ""

# 8. 근처 게임 검색
echo -e "${BLUE}[8] 근처 게임 검색 (반경 5km)${NC}"
echo "GET ${BASE_URL}/games/nearby?lat=37.5665&lng=126.9780&radius=5"
curl -X GET "${BASE_URL}/games/nearby?lat=37.5665&lng=126.9780&radius=5" | jq '.'
echo ""
echo ""

# 9. 모집 중 상태 게임 조회
echo -e "${BLUE}[9] '모집 중' 상태 게임 조회${NC}"
echo "GET ${BASE_URL}/games/status/모집_중"
curl -X GET "${BASE_URL}/games/status/모집_중" | jq '.'
echo ""
echo ""

echo -e "${GREEN}테스트 완료!${NC}"

