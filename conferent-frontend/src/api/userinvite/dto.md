# UserInvite DTO 명세서

## 📋 개요
사용자 초대 관리 API에서 사용되는 데이터 전송 객체(DTO) 명세서입니다.

## 🔧 API 엔드포인트

### GET /api/user-invites
**모든 사용자 초대 목록 조회**

**응답:**
```json
[
  {
    "id": 1,
    "user": {
      "id": 2,
      "name": "김철수",
      "email": "kim@example.com"
    },
    "roomRent": {
      "id": 1,
      "room": {
        "id": 1,
        "name": "회의실 A"
      },
      "rent": {
        "id": 1,
        "startTime": "2024-01-01T09:00:00",
        "endTime": "2024-01-01T10:00:00",
        "purpose": "팀 미팅"
      }
    },
    "status": "PENDING"
  }
]
```

### GET /api/user-invites/{id}
**ID로 사용자 초대 조회**

**파라미터:**
- `id` (number): 사용자 초대 ID

**응답:** (GET /api/user-invites와 동일한 구조, 단일 객체)

### GET /api/user-invites/user/{userId}
**사용자별 초대 목록 조회**

**파라미터:**
- `userId` (number): 사용자 ID

**응답:** (GET /api/user-invites와 동일)

### GET /api/user-invites/rent/{rentId}
**예약별 초대 목록 조회**

**파라미터:**
- `rentId` (number): 예약 ID

**응답:** (GET /api/user-invites와 동일)

### GET /api/user-invites/user/{userId}/status/{status}
**사용자별 상태별 초대 조회**

**파라미터:**
- `userId` (number): 사용자 ID
- `status` (string): 초대 상태 (PENDING/ACCEPTED/DECLINED)

**응답:** (GET /api/user-invites와 동일)

### POST /api/user-invites
**사용자 초대 생성**

**요청 본문:**
```json
{
  "userId": 2,
  "roomRentId": 1
}
```

**필드 설명:**
- `userId` (number, required): 사용자 ID
- `roomRentId` (number, required): 회의실-예약 연결 ID

**응답:** (GET /api/user-invites/{id}와 동일)

### PUT /api/user-invites/{id}/status
**초대 상태 업데이트**

**파라미터:**
- `id` (number): 사용자 초대 ID

**쿼리 파라미터:**
- `status` (string): 새로운 상태 (PENDING/ACCEPTED/DECLINED)

**응답:** (GET /api/user-invites/{id}와 동일)

### DELETE /api/user-invites/{id}
**사용자 초대 삭제**

**파라미터:**
- `id` (number): 사용자 초대 ID

**응답:** 204 No Content

### DELETE /api/user-invites/user/{userId}
**사용자별 모든 초대 삭제**

**파라미터:**
- `userId` (number): 사용자 ID

**응답:** 204 No Content

### DELETE /api/user-invites/room-rent/{roomRentId}
**RoomRent별 모든 초대 삭제**

**파라미터:**
- `roomRentId` (number): 회의실-예약 연결 ID

**응답:** 204 No Content

### GET /api/user-invites/exists
**초대 존재 여부 확인**

**쿼리 파라미터:**
- `userId` (number): 사용자 ID
- `roomRentId` (number): 회의실-예약 연결 ID

**응답:**
```json
true
```

### GET /api/user-invites/find
**특정 사용자와 RoomRent의 초대 조회**

**쿼리 파라미터:**
- `userId` (number): 사용자 ID
- `roomRentId` (number): 회의실-예약 연결 ID

**응답:** (GET /api/user-invites/{id}와 동일)

### GET /api/user-invites/user/{userId}/pending-count
**사용자별 대기 중인 초대 개수 조회**

**파라미터:**
- `userId` (number): 사용자 ID

**응답:**
```json
5
```

### GET /api/user-invites/rent/{rentId}/accepted-count
**예약별 수락된 초대 개수 조회**

**파라미터:**
- `rentId` (number): 예약 ID

**응답:**
```json
3
```

## 📊 데이터 타입

### UserInvite 객체
```typescript
interface UserInvite {
  id: number;
  user: User;
  roomRent: RoomRent;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
}
```

### CreateUserInviteRequest
```typescript
interface CreateUserInviteRequest {
  userId: number;
  roomRentId: number;
}
```

### UpdateInviteStatusRequest
```typescript
interface UpdateInviteStatusRequest {
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
}
```

### User 객체 (간소화)
```typescript
interface User {
  id: number;
  name: string;
  email: string;
}
```

### RoomRent 객체 (간소화)
```typescript
interface RoomRent {
  id: number;
  room: {
    id: number;
    name: string;
  };
  rent: {
    id: number;
    startTime: string; // ISO 8601
    endTime: string; // ISO 8601
    purpose: string;
  };
}
```

### InviteStatus 열거형
```typescript
enum InviteStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  DECLINED = 'DECLINED'
}
```

## ⚠️ 에러 응답

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "User and RoomRent are required",
  "path": "/api/user-invites"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "UserInvite not found with id: 999",
  "path": "/api/user-invites/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "UserInvite already exists for user 2 and roomRent 1",
  "path": "/api/user-invites"
}
```

## 📊 초대 상태 설명

- **PENDING**: 초대가 발송되었지만 아직 응답하지 않은 상태
- **ACCEPTED**: 사용자가 초대를 수락한 상태
- **DECLINED**: 사용자가 초대를 거절한 상태

## 🔗 관계 설명

- **UserInvite**는 **User**와 **RoomRent** 간의 N:M 관계를 표현하는 연결 테이블입니다
- 하나의 사용자는 여러 예약에 초대될 수 있습니다
- 하나의 예약은 여러 사용자를 초대할 수 있습니다
- 각 초대는 고유한 상태를 가집니다 