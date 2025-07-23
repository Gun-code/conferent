FROM nginx:alpine

# Nginx 설정 파일 복사 (SSL 설정 사용)
COPY nginx-ssl.conf /etc/nginx/nginx.conf

# SSL 인증서 디렉토리 생성
RUN mkdir -p /etc/letsencrypt/live/conferent.duckdns.org

# 포트 노출 (HTTP + HTTPS)
EXPOSE 80 443

# Nginx 시작
CMD ["nginx", "-g", "daemon off;"] 