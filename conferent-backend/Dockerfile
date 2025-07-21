# 멀티 스테이지 빌드로 이미지 크기 최적화
FROM gradle:8.5-jdk17-alpine as builder

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스크립트와 소스 코드 복사
COPY build.gradle .
COPY src src

# 빌드 실행
RUN gradle build -x test --no-daemon

# 런타임 스테이지
FROM eclipse-temurin:17-jre-alpine

# 작업 디렉토리 설정
WORKDIR /app

# curl 설치 (헬스체크용)
RUN apk add --no-cache curl

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"] 