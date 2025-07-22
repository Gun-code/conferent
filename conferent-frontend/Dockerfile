# 멀티 스테이지 빌드
FROM node:18 as builder

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사
COPY package*.json ./

# 의존성 설치 (빌드에 필요한 devDependencies 포함)
RUN npm ci

# 소스 코드 복사
COPY . .

# 빌드 (직접 vite 실행)
RUN node node_modules/vite/bin/vite.js build

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