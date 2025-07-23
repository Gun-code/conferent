# User DTO 명세서

## 📋 개요
사용자 관리 API에서 사용되는 데이터 전송 객체(DTO) 명세서입니다.

## 🔧 API 엔드포인트

### GET /api/users
**모든 사용자 목록 조회**

**응답:**
```json
[
  {
    "id": 1,
    "name": "홍길동",
    "email": "hong@example.com",
    "role": "USER"
  }
]
```

### GET /api/users/{id}
**ID로 사용자 조회**

**파라미터:**
- `id` (number): 사용자 ID

**응답:**
```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com",
  "role": "USER"
}
```

### GET /api/users/email/{email}
**이메일로 사용자 조회**

**파라미터:**
- `email` (string): 사용자 이메일

**응답:** (GET /api/users/{id}와 동일)

### POST /api/users
**사용자 생성**

**요청 본문:**
```json
{
  "name": "홍길동",
  "email": "hong@example.com",
  "password": "password123",
  "role": "USER"
}
```

**필드 설명:**
- `name` (string, required): 사용자 이름
- `email` (string, required): 이메일 (고유값)
- `password` (string, required): 비밀번호
- `role` (string, required): 역할 (USER/ADMIN)

**응답:**
```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com",
  "role": "USER"
}
```

### PUT /api/users/{id}
**사용자 수정**

**파라미터:**
- `id` (number): 사용자 ID

**요청 본문:** (POST와 동일, password는 선택사항)

**응답:** (POST와 동일)

### DELETE /api/users/{id}
**사용자 삭제**

**파라미터:**
- `id` (number): 사용자 ID

**응답:** 204 No Content

### GET /api/users/role/{role}
**역할별 사용자 조회**

**파라미터:**
- `role` (string): 역할 (USER/ADMIN)

**응답:** (GET /api/users와 동일)

### GET /api/users/{id}/exists
**사용자 존재 여부 확인**

**파라미터:**
- `id` (number): 사용자 ID

**응답:**
```json
true
```

### GET /api/users/email/{email}/exists
**이메일 중복 확인**

**파라미터:**
- `email` (string): 이메일

**응답:**
```json
true
```

## 📊 데이터 타입

### User 객체
```typescript
interface User {
  id: number;
  name: string;
  email: string;
  role: 'USER' | 'ADMIN';
}
```

### CreateUserRequest
```typescript
interface CreateUserRequest {
  name: string;
  email: string;
  password: string;
  role: 'USER' | 'ADMIN';
}
```

### UpdateUserRequest
```typescript
interface UpdateUserRequest {
  name?: string;
  email?: string;
  password?: string;
  role?: 'USER' | 'ADMIN';
}
```

### UserRole 열거형
```typescript
enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN'
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
  "path": "/api/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 999",
  "path": "/api/users/999"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "User with email 'hong@example.com' already exists",
  "path": "/api/users"
}
```

## 🔐 보안 고려사항

- 비밀번호는 응답에 포함되지 않습니다
- 이메일은 고유값으로 처리됩니다
- 역할은 USER 또는 ADMIN만 허용됩니다 