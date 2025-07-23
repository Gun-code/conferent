# 멀티 스테이지 빌드
FROM node:18-alpine as builder

# 빌드 도구 설치
RUN apk add --no-cache python3 make g++

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사
COPY package*.json ./

# 의존성 설치
RUN npm ci

# 소스 코드 복사
COPY . .

# 빌드
RUN npm run build

# 런타임 스테이지 - Nginx 사용
FROM nginx:alpine

# 빌드된 파일 복사
COPY --from=builder /app/dist /usr/share/nginx/html

# Nginx 설정 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 포트 노출
EXPOSE 3000

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"] 