# Conferent 데이터베이스 전략

## 📊 데이터베이스 환경별 설정

### 🚀 개발환경 (H2)
- **파일**: `application-dev.yml`
- **데이터베이스**: H2 인메모리
- **더미데이터**: `data-h2.sql` (자동 로드)
- **DDL 전략**: `create-drop` (애플리케이션 시작 시 테이블 생성, 종료 시 삭제)

### 🏭 운영환경 (MariaDB)
- **파일**: `application-prod.yml`
- **데이터베이스**: MariaDB
- **더미데이터**: `data-mariadb.sql` (수동 실행)
- **DDL 전략**: `validate` (스키마 검증만)

## 🎯 더미데이터 구성

### 👥 사용자 데이터
- **관리자**: 2명 (시스템 관리자, 운영 관리자)
- **일반 사용자**: 6명 (홍길동, 김철수, 이영희, 박민수, 정수진, 최동욱, 윤서연)

### 🏢 회의실 데이터
- **대회의실**: 20명 수용 (본관 3층)
- **소회의실 A/B**: 6명, 4명 수용 (본관 2층)
- **브레인스토밍룸**: 8명 수용 (별관 1층)
- **임원회의실**: 12명 수용 (본관 4층)
- **교육실 A/B**: 15명 수용 (별관 2층)
- **미디어룸**: 10명 수용 (본관 1층)
- **VIP 회의실**: 8명 수용 (본관 5층) - 운영환경만
- **세미나홀**: 50명 수용 (별관 3층) - 운영환경만

### 📅 예약 데이터
- **개발환경**: 오늘부터 6일 후까지 17개 예약
- **운영환경**: 오늘부터 2일 후까지 6개 예약

### 📧 초대 데이터
- **개발환경**: 다양한 상태(PENDING, ACCEPTED, DECLINED)의 초대
- **운영환경**: 기본적인 ACCEPTED 상태 초대

## 🚀 실행 방법

### 개발환경 실행
```bash
# 프로파일 지정하여 실행
./gradlew bootRun --args='--spring.profiles.active=dev'

# 또는 환경변수로 지정
export SPRING_PROFILES_ACTIVE=dev
./gradlew bootRun
```

### 운영환경 실행
```bash
# 1. MariaDB 데이터베이스 준비
mysql -u root -p
CREATE DATABASE conferent CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'conferent_user'@'%' IDENTIFIED BY 'conferent_password';
GRANT ALL PRIVILEGES ON conferent.* TO 'conferent_user'@'%';
FLUSH PRIVILEGES;
EXIT;

# 2. 초기 데이터 삽입
mysql -u conferent_user -p conferent < src/main/resources/data-mariadb.sql

# 3. 애플리케이션 실행
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## 🔧 데이터베이스 접속

### H2 콘솔 (개발환경)
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:conferent`
- **Username**: `sa`
- **Password**: (비어있음)

### MariaDB (운영환경)
- **Host**: localhost:3306
- **Database**: conferent
- **Username**: conferent_user
- **Password**: conferent_password

## 📋 API 테스트 계정

### 개발환경
```
관리자: admin@conferent.com / admin123
일반사용자: hong@conferent.com / user123
```

### 운영환경
```
시스템관리자: admin@conferent.com / admin123
운영관리자: operator@conferent.com / operator123
```

## 🔄 데이터 리셋

### 개발환경
- 애플리케이션 재시작 시 자동으로 데이터가 리셋됩니다.

### 운영환경
```sql
-- 모든 데이터 삭제 후 재삽입
TRUNCATE TABLE user_invites;
TRUNCATE TABLE room_rents;
TRUNCATE TABLE rents;
TRUNCATE TABLE users;
TRUNCATE TABLE rooms;

-- 초기 데이터 재삽입
source src/main/resources/data-mariadb.sql;
```

## ⚠️ 주의사항

1. **운영환경 비밀번호**: 실제 운영 시에는 기본 비밀번호를 반드시 변경하세요.
2. **데이터 백업**: 운영환경에서는 정기적인 데이터 백업을 수행하세요.
3. **보안**: 운영환경에서는 데이터베이스 접근 권한을 최소화하세요. 