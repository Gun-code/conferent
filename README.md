# Conferent - 회의실 예약 시스템 🏢

## 🚀 빠른 시작

### 📋 사전 요구사항
- Docker & Docker Compose
- Java 17+
- Node.js 18+

### 🌐 로컬 실행 방법

#### 개발/테스트 환경 (권장)
```bash
git clone https://github.com/Gun-code/conferent.git
cd conferent

# MariaDB + 핫 리로드 지원
docker-compose -f docker-compose.test.yml up --build
```

#### 프로덕션 환경 테스트
```bash
# nginx 포함, 프로덕션과 동일한 구조
docker-compose up --build
```

### 🔗 서비스 접근

**개발/테스트 환경:**
- **프론트엔드**: http://localhost:3000
- **백엔드 API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/
- **MariaDB**: localhost:3306

**프로덕션 환경:**
- **메인 서비스**: http://localhost (nginx를 통한 접근)

### 🔑 기본 로그인 정보
- **관리자**: `admin@conferent.com` / `admin123`
- **테스트 사용자**: `user@conferent.com` / `user123`

### 🔧 로컬 개발 실행

#### 백엔드
```bash
cd conferent-backend
./gradlew bootRun --args='--spring.profiles.active=local'  # MariaDB 사용
./gradlew bootRun --args='--spring.profiles.active=dev'    # H2 사용
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
├── conferent-backend/          # Spring Boot 백엔드
│   ├── src/main/java/
│   │   ├── controllers/        # API 컨트롤러
│   │   ├── services/          # 비즈니스 로직
│   │   ├── repositories/      # 데이터 접근 계층
│   │   ├── entities/          # JPA 엔티티
│   │   ├── dtos/             # 데이터 전송 객체
│   │   └── config/           # 설정 클래스
│   └── src/main/resources/
│       ├── application.yml    # 기본 설정
│       ├── application-dev.yml # H2 DB 설정
│       ├── application-local.yml # MariaDB 설정
│       └── application-prod.yml # 프로덕션 설정
├── conferent-frontend/         # Vue.js 프론트엔드
│   ├── src/
│   │   ├── components/        # 재사용 가능한 컴포넌트
│   │   ├── pages/            # 페이지 컴포넌트
│   │   ├── api/              # API 클라이언트
│   │   ├── store/            # 상태 관리
│   │   └── styles/           # CSS 스타일
│   └── Dockerfile.test       # 개발용 도커파일
├── docker-compose.yml         # 프로덕션 환경
├── docker-compose.test.yml    # 개발/테스트 환경
└── mysql/                     # MariaDB 초기화 스크립트
```

## 🎯 주요 기능
- 회의실 예약 관리
- 사용자 인증 및 권한 관리
- 실시간 예약 현황 확인
- 예약 충돌 방지
- 회의 참석자 초대 및 관리
- 관리자 대시보드

## 🚀 기술 스택

### 백엔드
- **Spring Boot 3.x** - 메인 프레임워크
- **Spring Security** - 인증 및 권한 관리
- **Spring Data JPA** - 데이터 접근 계층
- **MariaDB** - 메인 데이터베이스
- **H2** - 개발용 인메모리 데이터베이스
- **Gradle** - 빌드 도구

### 프론트엔드
- **Vue.js 3** - 프론트엔드 프레임워크
- **Vite** - 빌드 도구
- **Tailwind CSS** - 스타일링
- **Pinia** - 상태 관리

### 인프라
- **Docker & Docker Compose** - 컨테이너화
- **Nginx** - 리버스 프록시
- **MariaDB** - 데이터베이스

## 📝 프로젝트 회고

### 🎯 프로젝트를 하며 얻은 인사이트

#### 1. 혼자보다는 함께하는 느낌 – Cursor AI의 활용
이번 프로젝트에서는 Cursor AI를 적극적으로 활용하며 **작업 속도를 크게 높일 수 있었습니다.**  
특히 반복적인 코드를 빠르게 작성하거나, 구현 방향에 대한 아이디어를 잡을 때 큰 도움이 되었습니다.

에러가 발생했을 때 함께 고민해주는 느낌도 있었고, 코드 품질을 높이는 데에도 많은 기여를 해줬습니다.  
**개발을 ‘혼자서만 하는 일’이 아니라, 함께 고민해줄 도구와 함께할 수 있다는 가능성**을 느낀 시간이었다고 생각합니다.

#### 2. 잘 만들고 싶다는 마음이 오히려 복잡함이 되기도
초기에는 구조를 꼼꼼하게 나누고, 재사용성과 유지보수를 고려해 설계를 진행했습니다.  
그런데 실제로 구현해보니, 프로젝트 규모에 비해 **구조가 오히려 복잡해져 작업이 느려지는 상황**도 생겼습니다.

이 경험을 통해, **모든 프로젝트가 복잡한 구조를 필요로 하지는 않는다**는 것을 배울 수 있었습니다.  
앞으로는 ‘지금 이 프로젝트에 가장 적합한 구조가 무엇인지’부터 생각하며 설계를 시작하려고 합니다.

#### 3. 하고 싶은 게 많았던 만큼, 우선순위의 중요성도 체감
기능적으로 욕심을 많이 냈던 프로젝트였습니다. 다양한 시도를 하며 기술적인 재미도 있었지만,  
결과적으로는 **모든 기능을 충분히 완성도 있게 다듬기엔 시간이 부족했던 점**이 아쉬움으로 남았습니다.

하지만 그 덕분에, 어떤 기준으로 우선순위를 정하고 핵심 기능부터 마무리하는 것이 중요한지를 느낄 수 있었습니다.  
이런 시행착오 덕분에, 다음에는 **같은 시간 안에 더 명확하고 완성도 높은 결과를 만들 수 있겠다는 자신감**도 얻게 되었습니다.

---

### 🔧 다음 프로젝트에 적용할 개선점

1. **핵심 기능부터 완성도 높게 만들기**  
   하고 싶은 걸 다 담기보다, 사용자에게 꼭 필요한 기능부터 제대로 완성하는 게 중요하다는 걸 느꼈습니다.

2. **기술 스택을 욕심내지 않기**  
   새로운 기술에 대한 도전도 좋지만, 프로젝트의 목적과 상황에 맞는 적절한 선택이 더 좋은 결과를 만들어준다고 생각합니다.

3. **단순한 구조로 시작하고 점진적으로 확장하기**  
   처음부터 너무 많은 걸 고려하기보다, 실제 흐름 속에서 필요한 부분을 하나씩 다듬어나가는 방식이 더 자연스럽고 효율적이라는 걸 경험했습니다.

---

이번 프로젝트는 단순히 기능을 구현하는 것을 넘어,  
**개발자로서의 사고방식과 문제 해결 방법을 정리할 수 있었던 소중한 경험**이었습니다.  
완벽하진 않았지만, 실험하고 시도하며 배운 것들이 많았고,  
**다음 프로젝트에서는 더 나은 선택과 결과를 만들어낼 수 있을 것**이라는 믿음을 갖게 되었습니다.
