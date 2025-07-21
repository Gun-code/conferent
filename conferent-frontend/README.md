# Conferent Frontend - 3계층 구조

## 🏗️ 아키텍처

### 3계층 구조 (3-Layer Architecture)

```
src/
├── presentation/    # 🎨 프레젠테이션 계층 - UI/UX
│   ├── views/      # 페이지 컴포넌트
│   ├── components/ # 재사용 가능한 UI 컴포넌트
│   └── router/     # 라우팅 설정
├── business/       # 💼 비즈니스 계층 - 비즈니스 로직
│   ├── models/     # 도메인 모델 & 엔티티
│   ├── services/   # 비즈니스 서비스
│   └── stores/     # 상태 관리 (Pinia)
└── data/          # 💾 데이터 계층 - 데이터 접근
    ├── api/       # API 클라이언트
    └── repositories/ # 데이터 리포지토리
```

## 📋 계층별 책임

### 🎨 Presentation Layer
- **목적**: 사용자와의 상호작용 담당
- **포함**: Vue 컴포넌트, 뷰, 라우팅
- **책임**: 
  - UI 렌더링
  - 사용자 입력 처리
  - 화면 전환 관리

### 💼 Business Layer  
- **목적**: 비즈니스 로직 처리
- **포함**: 도메인 모델, 비즈니스 서비스, 상태 관리
- **책임**:
  - 도메인 규칙 검증
  - 비즈니스 워크플로우 관리
  - 전역 상태 관리

### 💾 Data Layer
- **목적**: 외부 데이터 소스와의 통신
- **포함**: API 클라이언트, 리포지토리
- **책임**:
  - HTTP 통신
  - 데이터 변환 (DTO ↔ Model)
  - 에러 처리

## 🔄 의존성 방향

```
Presentation → Business → Data
```

- Presentation은 Business에만 의존
- Business는 Data에만 의존  
- Data는 외부 API에 의존

## 🚀 시작하기

```bash
# 의존성 설치
npm install

# 개발 서버 실행
npm run dev

# 빌드
npm run build
```

## 🛠️ 기술 스택

- **Vue 3** (Composition API)
- **TypeScript**
- **Pinia** (상태 관리)
- **Vue Router** (라우팅)
- **Tailwind CSS** (스타일링)
- **Vite** (빌드 도구)
- **Axios** (HTTP 클라이언트)

## 📂 주요 파일 구조

```
src/
├── data/
│   ├── api/
│   │   └── ApiClient.ts              # 중앙화된 API 클라이언트
│   └── repositories/
│       ├── ReservationDataRepository.ts  # 예약 데이터 접근
│       └── RoomDataRepository.ts          # 회의실 데이터 접근
├── business/
│   ├── models/
│   │   ├── ReservationModel.ts       # 예약 도메인 모델
│   │   └── RoomModel.ts              # 회의실 도메인 모델
│   ├── services/
│   │   ├── ReservationBusinessService.ts  # 예약 비즈니스 로직
│   │   └── RoomBusinessService.ts         # 회의실 비즈니스 로직
│   └── stores/
│       ├── ReservationStore.ts       # 예약 상태 관리
│       └── RoomStore.ts              # 회의실 상태 관리
└── presentation/
    ├── views/                        # 페이지 컴포넌트들
    ├── components/                   # 재사용 UI 컴포넌트들
    └── router/
        └── index.ts                  # 라우팅 설정
```

## 🔧 개발 가이드라인

### 1. 계층 분리 원칙
- 각 계층은 명확한 책임을 가짐
- 상위 계층만 하위 계층에 의존
- 순환 의존성 금지

### 2. 네이밍 규칙
- **Models**: `xxxModel.ts`, `xxxEntity.ts`
- **Services**: `xxxBusinessService.ts`
- **Repositories**: `xxxDataRepository.ts`
- **Stores**: `xxxStore.ts`
- **Views**: `xxxView.vue`
- **Components**: `xxxComponent.vue`

### 3. 파일 구조 규칙
- 폴더명과 파일명 중복 방지
- 계층별로 명확한 분리
- 역할에 따른 일관된 네이밍

## 🎯 장점

1. **명확한 책임 분리**: 각 계층이 고유한 역할을 담당
2. **유지보수성**: 변경 사항이 해당 계층에만 국한
3. **테스트 용이성**: 계층별 독립적 테스트 가능
4. **확장성**: 새로운 기능 추가 시 구조 유지
5. **가독성**: 직관적인 폴더 구조로 코드 위치 파악 용이 