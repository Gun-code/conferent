# RoomRent DTO 명세서

## 📋 개요
회의실-예약 연결 관리 API에서 사용되는 데이터 전송 객체(DTO) 명세서입니다.

## 🔧 API 엔드포인트

### GET /api/room-rents
**모든 회의실-예약 연결 목록 조회**

**응답:**
```json
[
  {
    "id": 1,
    "room": {
      "id": 1,
      "name": "회의실 A",
      "location": "3층"
    },
    "rent": {
      "id": 1,
      "startTime": "2024-01-01T09:00:00",
      "endTime": "2024-01-01T10:00:00",
      "purpose": "팀 미팅"
    }
  }
]
```

### GET /api/room-rents/{id}
**ID로 회의실-예약 연결 조회**

**파라미터:**
- `id` (number): 회의실-예약 연결 ID

**응답:** (GET /api/room-rents와 동일한 구조, 단일 객체)

### POST /api/room-rents
**회의실-예약 연결 생성**

**요청 본문:**
```json
{
  "roomId": 1,
  "rentId": 1
}
```

**필드 설명:**
- `roomId` (number, required): 회의실 ID
- `rentId` (number, required): 예약 ID

**응답:** (GET /api/room-rents/{id}와 동일)

### PUT /api/room-rents/{id}
**회의실-예약 연결 수정**

**파라미터:**
- `id` (number): 회의실-예약 연결 ID

**요청 본문:** (POST와 동일)

**응답:** (POST와 동일)

### DELETE /api/room-rents/{id}
**회의실-예약 연결 삭제**

**파라미터:**
- `id` (number): 회의실-예약 연결 ID

**응답:** 204 No Content

### GET /api/room-rents/rent/{rentId}
**예약별 회의실-예약 연결 조회**

**파라미터:**
- `rentId` (number): 예약 ID

**응답:** (GET /api/room-rents와 동일)

### GET /api/room-rents/room/{roomId}
**회의실별 회의실-예약 연결 조회**

**파라미터:**
- `roomId` (number): 회의실 ID

**응답:** (GET /api/room-rents와 동일)

### GET /api/room-rents/find
**특정 회의실과 예약의 연결 조회**

**쿼리 파라미터:**
- `rentId` (number): 예약 ID
- `roomId` (number): 회의실 ID

**응답:** (GET /api/room-rents/{id}와 동일)

### GET /api/room-rents/{id}/exists
**회의실-예약 연결 존재 여부 확인**

**파라미터:**
- `id` (number): 회의실-예약 연결 ID

**응답:**
```json
true
```

### GET /api/room-rents/exists
**특정 회의실과 예약의 연결 존재 여부 확인**

**쿼리 파라미터:**
- `rentId` (number): 예약 ID
- `roomId` (number): 회의실 ID

**응답:**
```json
true
```

## 📊 데이터 타입

### RoomRent 객체
```typescript
interface RoomRent {
  id: number;
  room: Room;
  rent: Rent;
}
```

### CreateRoomRentRequest
```typescript
interface CreateRoomRentRequest {
  roomId: number;
  rentId: number;
}
```

### UpdateRoomRentRequest
```typescript
interface UpdateRoomRentRequest {
  roomId?: number;
  rentId?: number;
}
```

### Room 객체 (간소화)
```typescript
interface Room {
  id: number;
  name: string;
  location: string;
}
```

### Rent 객체 (간소화)
```typescript
interface Rent {
  id: number;
  startTime: string; // ISO 8601
  endTime: string; // ISO 8601
  purpose: string;
}
```

## ⚠️ 에러 응답

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Room and Rent are required",
  "path": "/api/room-rents"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "RoomRent not found with id: 999",
  "path": "/api/room-rents/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "RoomRent already exists for room 1 and rent 1",
  "path": "/api/room-rents"
}
```

## 🔗 관계 설명

- **RoomRent**는 **Room**과 **Rent** 간의 N:M 관계를 표현하는 연결 테이블입니다
- 하나의 예약은 여러 회의실을 가질 수 있습니다
- 하나의 회의실은 여러 예약에 포함될 수 있습니다
- 각 연결은 고유한 ID를 가집니다 