# Room DTO 명세서

## 📋 개요
회의실 관리 API에서 사용되는 데이터 전송 객체(DTO) 명세서입니다.

## 🔧 API 엔드포인트

### GET /api/rooms
**모든 회의실 목록 조회**

**응답:**
```json
[
  {
    "id": 1,
    "name": "회의실 A",
    "location": "3층",
    "capacity": 10,
    "description": "3층 메인 회의실"
  }
]
```

### GET /api/rooms/{id}
**ID로 회의실 조회**

**파라미터:**
- `id` (number): 회의실 ID

**응답:**
```json
{
  "id": 1,
  "name": "회의실 A",
  "location": "3층",
  "capacity": 10,
  "description": "3층 메인 회의실"
}
```

### POST /api/rooms
**회의실 생성**

**요청 본문:**
```json
{
  "name": "회의실 A",
  "location": "3층",
  "capacity": 10,
  "description": "3층 메인 회의실"
}
```

**필드 설명:**
- `name` (string, required): 회의실 이름
- `location` (string, required): 위치
- `capacity` (number, required): 수용 인원
- `description` (string, optional): 설명

**응답:**
```json
{
  "id": 1,
  "name": "회의실 A",
  "location": "3층",
  "capacity": 10,
  "description": "3층 메인 회의실"
}
```

### PUT /api/rooms/{id}
**회의실 수정**

**파라미터:**
- `id` (number): 회의실 ID

**요청 본문:** (POST와 동일)

**응답:** (POST와 동일)

### DELETE /api/rooms/{id}
**회의실 삭제**

**파라미터:**
- `id` (number): 회의실 ID

**응답:** 204 No Content

### GET /api/rooms/capacity/{minCapacity}
**용량별 회의실 검색**

**파라미터:**
- `minCapacity` (number): 최소 수용 인원

**응답:** (GET /api/rooms와 동일)

### GET /api/rooms/location/{location}
**위치별 회의실 검색**

**파라미터:**
- `location` (string): 위치

**응답:** (GET /api/rooms와 동일)

### GET /api/rooms/{id}/exists
**회의실 존재 여부 확인**

**파라미터:**
- `id` (number): 회의실 ID

**응답:**
```json
true
```

## 📊 데이터 타입

### Room 객체
```typescript
interface Room {
  id: number;
  name: string;
  location: string;
  capacity: number;
  description?: string;
}
```

### CreateRoomRequest
```typescript
interface CreateRoomRequest {
  name: string;
  location: string;
  capacity: number;
  description?: string;
}
```

## ⚠️ 에러 응답

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/rooms"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Room not found with id: 999",
  "path": "/api/rooms/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Room with name '회의실 A' already exists",
  "path": "/api/rooms"
}
``` 