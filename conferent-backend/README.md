# 🚀 Conferent Backend

Java + Spring Boot 기반의 회의실 예약 시스템 백엔드 API입니다.

## 📁 모듈 구조

### Domain 모듈 (`conferent-domain`)
핵심 비즈니스 로직과 도메인 규칙을 담당합니다.

```
conferent-domain/
├── entities/
│   ├── User.java              # 사용자 엔티티
│   ├── Room.java              # 회의실 엔티티
│   └── Reservation.java       # 예약 엔티티
├── repositories/              # 리포지토리 인터페이스
├── services/                  # 도메인 서비스
└── valueobjects/
    └── ReservationTime.java   # 예약 시간 값 객체
```

**주요 특징:**
- 외부 의존성 없음 (순수 Java)
- 비즈니스 규칙과 불변식 구현
- 도메인 이벤트 처리

### Application 모듈 (`conferent-application`)
애플리케이션의 유스케이스를 정의합니다.

```
conferent-application/
├── usecases/
│   ├── reservation/
│   │   ├── CreateReservationUseCase.java
│   │   └── CancelReservationUseCase.java
│   └── room/
│       ├── CreateRoomUseCase.java
│       └── GetRoomsUseCase.java
└── dto/                       # 데이터 전송 객체
```

**주요 특징:**
- 트랜잭션 경계 정의
- 도메인 오케스트레이션
- 외부 서비스 호출 조정

### Interface 모듈 (`conferent-interface`)
외부와의 통신을 담당하는 REST API입니다.

```
conferent-interface/
└── api/
    ├── ReservationController.java
    ├── RoomController.java
    └── UserController.java
```

**주요 특징:**
- HTTP 요청/응답 처리
- 입력 유효성 검증
- 예외 처리 및 응답 변환

### Infrastructure 모듈 (`conferent-infrastructure`)
데이터베이스, 외부 서비스 등 인프라스트럭처 관련 구현체입니다.

```
conferent-infrastructure/
├── persistence/
│   ├── entity/                # JPA 엔티티
│   ├── UserRepositoryImpl.java
│   ├── RoomRepositoryImpl.java
│   └── ReservationRepositoryImpl.java
└── notification/
    └── WebPushNotificationService.java
```

**주요 특징:**
- JPA를 통한 데이터 영속화
- 도메인 객체 ↔ JPA 엔티티 변환
- 외부 알림 서비스 구현

### Main 모듈 (`conferent-main`)
애플리케이션의 진입점이자 실행 가능한 JAR를 생성합니다.

```
conferent-main/
├── ConferentApplication.java  # Spring Boot 메인 클래스
└── resources/
    └── application.yml        # 설정 파일
```

## 🔧 빌드 및 실행

### 요구사항
- Java 17+
- Gradle 7.0+

### 개발환경 실행
```bash
# H2 데이터베이스로 실행
./gradlew :conferent-main:bootRun

# 또는 profile 지정
./gradlew :conferent-main:bootRun --args='--spring.profiles.active=dev'
```

### 운영환경 빌드
```bash
# 전체 빌드
./gradlew build

# 실행 가능한 JAR 생성
./gradlew :conferent-main:bootJar

# 실행
java -jar conferent-main/build/libs/conferent-main-1.0.0.jar
```

### 테스트 실행
```bash
# 전체 모듈 테스트
./gradlew test

# 특정 모듈 테스트
./gradlew :conferent-domain:test
```

## 📊 API 엔드포인트

### 회의실 관리
- `GET /api/rooms` - 회의실 목록 조회
- `GET /api/rooms?minCapacity=10` - 최소 수용인원으로 필터링
- `POST /api/rooms` - 회의실 생성 (관리자만)

### 예약 관리
- `POST /api/reservations` - 예약 생성
- `DELETE /api/reservations/{id}` - 예약 취소
- `GET /api/reservations/user/{userId}` - 사용자별 예약 목록

### 요청/응답 예시

#### 예약 생성
```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -H "User-Id: 1" \
  -d '{
    "roomId": 1,
    "startTime": "2024-01-15T09:00:00",
    "endTime": "2024-01-15T10:00:00",
    "purpose": "팀 미팅"
  }'
```

#### 응답
```json
{
  "id": 1,
  "userId": 1,
  "roomId": 1,
  "roomName": "대회의실",
  "startTime": "2024-01-15T09:00:00",
  "endTime": "2024-01-15T10:00:00",
  "purpose": "팀 미팅",
  "status": "PENDING",
  "createdAt": "2024-01-14T15:30:00",
  "updatedAt": "2024-01-14T15:30:00"
}
```

## 🗄️ 데이터베이스 스키마

### Users 테이블
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Rooms 테이블
```sql
CREATE TABLE rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Reservations 테이블
```sql
CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    purpose TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id)
);
```

## 🏗️ 아키텍처 원칙

### 의존성 방향
```
Main → Interface → Application → Domain
           ↓            ↓
    Infrastructure → Domain
```

### 주요 패턴
- **Repository Pattern**: 데이터 접근 추상화
- **Use Case Pattern**: 애플리케이션 로직 캡슐화
- **Domain Service Pattern**: 복잡한 도메인 로직 처리
- **Value Object Pattern**: 불변 값 객체 구현

## 🔒 보안 고려사항

### 현재 구현
- 간단한 헤더 기반 사용자 식별 (`User-Id` 헤더)
- 기본적인 권한 검증 (예약 소유자 확인)

### 추후 개선 방향
- JWT 기반 인증/인가
- Spring Security 적용
- OAuth 2.0 소셜 로그인
- API Rate Limiting

## 📈 성능 최적화

### 적용된 최적화
- JPA 지연 로딩 전략
- 적절한 인덱스 설정
- 트랜잭션 범위 최소화

### 추후 개선 방향
- 쿼리 최적화 (N+1 문제 해결)
- 캐싱 전략 (Redis)
- 데이터베이스 커넥션 풀 튜닝

## 🧪 테스트 전략

### 단위 테스트
- 도메인 로직 테스트
- 유스케이스 테스트
- 리포지토리 테스트

### 통합 테스트
- API 엔드포인트 테스트
- 데이터베이스 연동 테스트

### 테스트 실행
```bash
# 도메인 로직 테스트
./gradlew :conferent-domain:test

# 애플리케이션 로직 테스트
./gradlew :conferent-application:test

# API 테스트
./gradlew :conferent-interface:test
``` 