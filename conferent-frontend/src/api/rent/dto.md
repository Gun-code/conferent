# Rent DTO 명세서

## 📋 개요
예약 관리 API에서 사용되는 데이터 전송 객체(DTO) 명세서입니다.

## 🔧 API 엔드포인트

### GET /api/rents
**모든 예약 목록 조회**

**응답:**
```json
[
  {
    "id": 1,
    "startTime": "2024-01-01T09:00:00",
    "endTime": "2024-01-01T10:00:00",
    "purpose": "팀 미팅",
    "description": "월간 팀 미팅",
    "creator": {
      "id": 1,
      "name": "홍길동",
      "email": "hong@example.com"
    },
    "rooms": [
      {
        "id": 1,
        "name": "회의실 A",
        "location": "3층"
      }
    ]
  }
]
```

### GET /api/rents/{id}
**ID로 예약 조회**

**파라미터:**
- `id` (number): 예약 ID

**응답:** (GET /api/rents와 동일한 구조, 단일 객체)

### POST /api/rents
**예약 생성**

**요청 본문:**
```json
{
  "startTime": "2024-01-01T09:00:00",
  "endTime": "2024-01-01T10:00:00",
  "purpose": "팀 미팅",
  "description": "월간 팀 미팅",
  "roomIds": [1, 2],
  "inviteeIds": [2, 3]
}
```

**필드 설명:**
- `startTime` (string, required): 시작 시간 (ISO 8601 형식)
- `endTime` (string, required): 종료 시간 (ISO 8601 형식)
- `purpose` (string, required): 예약 목적
- `description` (string, optional): 설명
- `roomIds` (Array<number>, required): 회의실 ID 목록
- `inviteeIds` (Array<number>, optional): 초대할 사용자 ID 목록

**응답:** (GET /api/rents/{id}와 동일)

### PUT /api/rents/{id}
**예약 수정**

**파라미터:**
- `id` (number): 예약 ID

**요청 본문:** (POST와 동일)

**응답:** (POST와 동일)

### DELETE /api/rents/{id}
**예약 삭제**

**파라미터:**
- `id` (number): 예약 ID

**응답:** 204 No Content

### GET /api/rents/creator/{creatorId}
**생성자별 예약 조회**

**파라미터:**
- `creatorId` (number): 생성자 ID

**응답:** (GET /api/rents와 동일)

### GET /api/rents/date-range
**날짜 범위별 예약 조회**

**쿼리 파라미터:**
- `startDate` (string): 시작 날짜 (ISO 8601)
- `endDate` (string): 종료 날짜 (ISO 8601)

**응답:** (GET /api/rents와 동일)

### GET /api/rents/upcoming
**예정된 예약 조회**

**쿼리 파라미터:**
- `fromTime` (string): 기준 시간 (ISO 8601)

**응답:** (GET /api/rents와 동일)

### GET /api/rents/search
**목적별 예약 검색**

**쿼리 파라미터:**
- `purpose` (string): 검색할 목적

**응답:** (GET /api/rents와 동일)

### GET /api/rents/room/{roomId}
**회의실별 예약 조회**

**파라미터:**
- `roomId` (number): 회의실 ID

**응답:** (GET /api/rents와 동일)

### GET /api/rents/{id}/exists
**예약 존재 여부 확인**

**파라미터:**
- `id` (number): 예약 ID

**응답:**
```json
true
```

### POST /api/rents/check-conflict
**시간 충돌 확인**

**요청 본문:**
```json
{
  "roomIds": [1, 2],
  "startTime": "2024-01-01T09:00:00",
  "endTime": "2024-01-01T10:00:00",
  "excludeRentId": 5
}
```

**필드 설명:**
- `roomIds` (Array<number>, required): 회의실 ID 목록
- `startTime` (string, required): 시작 시간
- `endTime` (string, required): 종료 시간
- `excludeRentId` (number, optional): 제외할 예약 ID

**응답:**
```json
true
```

## 📊 데이터 타입

### Rent 객체
```typescript
interface Rent {
  id: number;
  startTime: string; // ISO 8601
  endTime: string; // ISO 8601
  purpose: string;
  description?: string;
  creator: User;
  rooms: Room[];
}
```

### CreateRentRequest
```typescript
interface CreateRentRequest {
  startTime: string; // ISO 8601
  endTime: string; // ISO 8601
  purpose: string;
  description?: string;
  roomIds: number[];
  inviteeIds?: number[];
}
```

### UpdateRentRequest
```typescript
interface UpdateRentRequest {
  startTime?: string; // ISO 8601
  endTime?: string; // ISO 8601
  purpose?: string;
  description?: string;
  roomIds?: number[];
  inviteeIds?: number[];
}
```

### ConflictCheckRequest
```typescript
interface ConflictCheckRequest {
  roomIds: number[];
  startTime: string; // ISO 8601
  endTime: string; // ISO 8601
  excludeRentId?: number;
}
```

## ⚠️ 에러 응답

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "End time must be after start time",
  "path": "/api/rents"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Rent not found with id: 999",
  "path": "/api/rents/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Time conflict detected for the specified rooms",
  "path": "/api/rents"
}
```

## 📅 시간 형식

- 모든 시간은 ISO 8601 형식을 사용합니다
- 예: `2024-01-01T09:00:00`
- 시간대는 서버 시간대를 기준으로 합니다 