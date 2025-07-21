# Conferent - 회의실 예약 시스템 🏢

## 🚀 빠른 시작

### 📋 사전 요구사항
- Docker & Docker Compose
- Java 17+
- Node.js 18+

### 🎯 DB 환경 설정

이 프로젝트는 **프로필에 따라 다른 데이터베이스**를 사용합니다:

| **프로필** | **데이터베이스** | **용도** |
|-----------|----------------|---------|
| `dev` | H2 (메모리) | 개발/테스트 |
| `prod` | 호스트 MariaDB | 운영 환경 |

### 🐳 Docker 실행 방법

#### 1️⃣ H2 DB 사용 (개발환경)
```bash
# 기본값은 dev 프로필 (H2 DB)
docker-compose up -d

# 또는 명시적으로 설정
SPRING_PROFILES_ACTIVE=dev docker-compose up -d
```

#### 2️⃣ 호스트 MariaDB 사용 (운영환경)
```bash
# 먼저 호스트에 MariaDB 설치 및 설정
sudo apt install mariadb-server  # Ubuntu
# 또는
brew install mariadb  # macOS

# MariaDB 설정
mysql -u root -p
CREATE DATABASE conferent;
CREATE USER 'conferent_user'@'localhost' IDENTIFIED BY 'conferent_password';
GRANT ALL PRIVILEGES ON conferent.* TO 'conferent_user'@'localhost';
FLUSH PRIVILEGES;

# prod 프로필로 Docker 실행
SPRING_PROFILES_ACTIVE=prod docker-compose up -d
```

### 🌐 접속 정보

- **백엔드 API**: http://localhost:8080
- **프론트엔드**: http://localhost:3000
- **H2 Console** (dev 프로필): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:conferent`
  - Username: `sa`
  - Password: (비어있음)

### 🔧 로컬 개발 실행

#### 백엔드
```bash
cd conferent-backend
./gradlew bootRun --args='--spring.profiles.active=dev'  # H2 사용
./gradlew bootRun --args='--spring.profiles.active=prod' # MariaDB 사용
```

#### 프론트엔드
```bash
cd conferent-frontend
npm install
npm run dev
```

### 📁 프로젝트 구조
```
conferent/
├── conferent-backend/     # Spring Boot 백엔드
├── conferent-frontend/    # React 프론트엔드
├── docker-compose.yml     # Docker 설정
└── README.md
```

## 🎯 주요 기능
- 회의실 예약 관리
- 사용자 인증 및 권한 관리
- 실시간 예약 현황 확인
- 예약 충돌 방지 