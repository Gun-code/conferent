#!/bin/bash

# EC2 배포 스크립트 (Linux/macOS)
# 사용법: ./deploy.sh <EC2_IP> <USER> <KEY_FILE>

if [ $# -lt 3 ]; then
    echo "사용법: $0 <EC2_IP> <USER> <KEY_FILE>"
    echo "예시: $0 3.34.123.45 ubuntu ~/.ssh/my-key.pem"
    exit 1
fi

SERVER=$1
USER=$2
KEYFILE=$3

echo "🚀 Conferent 배포 시작..."

# 1. 백엔드 빌드
echo "📦 백엔드 빌드 중..."
cd conferent-backend
./gradlew clean build
if [ $? -ne 0 ]; then
    echo "❌ 백엔드 빌드 실패!"
    exit 1
fi
cd ..

# 2. 프론트엔드 빌드
echo "📦 프론트엔드 빌드 중..."
cd conferent-frontend
npm install
npm run build
if [ $? -ne 0 ]; then
    echo "❌ 프론트엔드 빌드 실패!"
    exit 1
fi
cd ..

# 3. 원격 디렉토리 생성
echo "📁 원격 디렉토리 준비 중..."
ssh -i $KEYFILE $USER@$SERVER "mkdir -p ~/conferent/conferent-backend/build/libs"
ssh -i $KEYFILE $USER@$SERVER "mkdir -p ~/conferent/conferent-frontend"
ssh -i $KEYFILE $USER@$SERVER "mkdir -p ~/conferent/nginx"

# 4. 파일 전송
echo "🔄 EC2로 파일 전송 중..."
scp -i $KEYFILE -r conferent-backend/build/libs/*.jar $USER@$SERVER:~/conferent/conferent-backend/build/libs/
scp -i $KEYFILE conferent-backend/Dockerfile.prod $USER@$SERVER:~/conferent/conferent-backend/
scp -i $KEYFILE -r conferent-frontend/dist $USER@$SERVER:~/conferent/conferent-frontend/
scp -i $KEYFILE conferent-frontend/Dockerfile.prod $USER@$SERVER:~/conferent/conferent-frontend/
scp -i $KEYFILE conferent-frontend/nginx.conf $USER@$SERVER:~/conferent/conferent-frontend/
scp -i $KEYFILE docker-compose.prod.yml $USER@$SERVER:~/conferent/
scp -i $KEYFILE -r nginx/ $USER@$SERVER:~/conferent/

# 5. 도커 실행
echo "🐳 도커 컨테이너 실행 중..."
ssh -i $KEYFILE $USER@$SERVER "cd ~/conferent && docker-compose -f docker-compose.prod.yml down"
ssh -i $KEYFILE $USER@$SERVER "cd ~/conferent && docker-compose -f docker-compose.prod.yml up --build -d"

echo "✅ 배포 완료!"
echo "🌐 서비스 접속: http://$SERVER"

# 6. 서비스 상태 확인
echo "📊 서비스 상태 확인 중..."
ssh -i $KEYFILE $USER@$SERVER "cd ~/conferent && docker-compose -f docker-compose.prod.yml ps" 