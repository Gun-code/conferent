# 🚀 Conferent Backend

Java + Spring Boot 기반의 회의실 예약 시스템 백엔드 API입니다.

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

### Swagger

API 문서화 및 테스트를 위한 Swagger UI가 제공됩니다.

#### 접속 방법
- **개발환경**: http://localhost:8080/swagger-ui.html
- **운영환경**: http://localhost:8080/swagger-ui.html

#### 주요 기능
- **API 문서 자동 생성**: 컨트롤러의 어노테이션 기반
- **인터랙티브 테스트**: 브라우저에서 직접 API 호출 가능
- **요청/응답 스키마**: JSON 형태로 자동 문서화
- **API 그룹화**: 도메인별로 API 분류


### H2

개발환경에서 사용하는 인메모리 데이터베이스입니다.

#### 접속 방법
- **H2 콘솔**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:conferent`
- **사용자명**: `SA`
- **비밀번호**: (비어있음)

#### 더미 데이터
애플리케이션 시작 시 자동으로 다음 데이터가 로드됩니다:

- **사용자**: 8명 (관리자 1명, 일반 사용자 7명)
- **회의실**: 8개 (대회의실, 소회의실, 브레인스토밍룸 등)
- **예약**: 17개 (오늘부터 일주일간)
- **초대**: 25개 (다양한 상태의 초대 데이터)


## 📁 디렉터리 구조

```
conferent/
├── entities/
│   ├── Room.java                # 회의실 엔티티
│   ├── User.java                # 사용자 엔티티
│   ├── Rent.java                # 예약 엔티티 (기본 예약 정보)
│   ├── RoomRent.java            # 회의실-예약 N:M 연결
│   └── UserInvite.java          # 예약-초대된 사용자 N:M 연결
│
├── repositories/
│   ├── room/
│   │   ├── RoomRepository.java
│   │   └── RoomRepositoryImpl.java
│   ├── user/
│   │   ├── UserRepository.java
│   │   └── UserRepositoryImpl.java
│   ├── rent/
│       ├── RentRepository.java
│       ├── RentRepositoryImpl.java
│       ├── RoomRentRepository.java
│       ├── RoomRentRepositoryImpl.java
│       ├── UserInviteRepository.java
│       └── UserInviteRepositoryImpl.java
│
├── services/
│   ├── room/
│   │   ├── RoomService.java
│   │   └── impl/
│   │       └── RoomServiceImpl.java
│   ├── user/
│   │   ├── UserService.java
│   │   └── impl/
│   │       └── UserServiceImpl.java
│   ├── rent/
│   │   ├── RentService.java
│   │   ├── RoomRentService.java
│   │   ├── InvitationService.java
│   │   └── impl/
│   │       ├── RentServiceImpl.java
│   │       ├── RoomRentServiceImpl.java
│   │       └── InvitationServiceImpl.java
│
├── controllers/
│   ├── room/
│   │   └── RoomController.java
│   ├── user/
│   │   └── UserController.java
│   └── rent/
│       ├── RentController.java
│       ├── RoomRentController.java
│       └── InvitationController.java
│
├── dtos/
│   ├── room/
│   │   ├── RoomRequest.java
│   │   └── RoomResponse.java
│   ├── user/
│   │   ├── UserRequest.java
│   │   └── UserResponse.java
│   └── rent/
│       ├── CreateRentRequest.java
│       ├── RentResponse.java
│       ├── InviteUserRequest.java
│       └── InviteeResponse.java
│
├── enums/
│   ├── Role.java                # USER, ADMIN
│   └── InviteStatus.java       # PENDING, ACCEPTED, DECLINED
│
└── exceptions/
    ├── NotFoundException.java
    ├── DuplicateReservationException.java
    └── GlobalExceptionHandler.java


```