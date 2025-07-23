# EC2 배포 스크립트 (Windows PowerShell)
# 사용법: .\deploy.ps1 -server "your-ec2-ip" -user "ubuntu" -keyfile "path/to/your-key.pem" [-skipBuild]

param(
    [Parameter(Mandatory=$true)]
    [string]$server,
    
    [Parameter(Mandatory=$false)]
    [string]$user = "ubuntu",
    
    [Parameter(Mandatory=$true)]
    [string]$keyfile,
    
    [Parameter(Mandatory=$false)]
    [switch]$skipBuild = $false
)

Write-Host "🚀 Conferent 배포 시작..." -ForegroundColor Green

if (-not $skipBuild) {
    # 1. 백엔드 빌드
    Write-Host "📦 백엔드 빌드 중..." -ForegroundColor Yellow
    Set-Location conferent-backend
    if (Test-Path ".\gradlew.bat") {
        .\gradlew.bat clean build
    } else {
        Write-Host "⚠️ gradlew.bat이 없습니다. gradle wrapper를 생성합니다..." -ForegroundColor Yellow
        gradle wrapper
        .\gradlew.bat clean build
    }
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ 백엔드 빌드 실패!" -ForegroundColor Red
        exit 1
    }
    Set-Location ..

    # 2. 프론트엔드 빌드
    Write-Host "📦 프론트엔드 빌드 중..." -ForegroundColor Yellow
    Set-Location conferent-frontend
    npm install
    npm run build
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ 프론트엔드 빌드 실패!" -ForegroundColor Red
        exit 1
    }
    Set-Location ..
} else {
    Write-Host "⚡ 빌드 건너뛰기 - 기존 빌드 파일 사용" -ForegroundColor Cyan
}

# 3. 빌드 파일 존재 확인
Write-Host "📁 빌드 파일 확인 중..." -ForegroundColor Yellow
if (-not (Test-Path "conferent-backend/build/libs/*.jar")) {
    Write-Host "❌ 백엔드 JAR 파일이 없습니다. 빌드를 먼저 실행하세요!" -ForegroundColor Red
    exit 1
}
if (-not (Test-Path "conferent-frontend/dist/index.html")) {
    Write-Host "❌ 프론트엔드 dist 폴더가 없습니다. 빌드를 먼저 실행하세요!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ 모든 빌드 파일 확인 완료" -ForegroundColor Green

# 4. SCP로 파일 전송
Write-Host "🔄 EC2로 파일 전송 중..." -ForegroundColor Yellow

# 원격 디렉토리 생성
ssh -i $keyfile $user@$server "mkdir -p ~/conferent/conferent-backend/build/libs"
ssh -i $keyfile $user@$server "mkdir -p ~/conferent/conferent-frontend"
ssh -i $keyfile $user@$server "mkdir -p ~/conferent/nginx"

# 파일 전송
scp -i $keyfile -r conferent-backend/build/libs/*.jar $user@$server`:~/conferent/conferent-backend/build/libs/
scp -i $keyfile conferent-backend/Dockerfile.prod $user@$server`:~/conferent/conferent-backend/
scp -i $keyfile -r conferent-frontend/dist $user@$server`:~/conferent/conferent-frontend/
scp -i $keyfile conferent-frontend/Dockerfile.prod $user@$server`:~/conferent/conferent-frontend/
scp -i $keyfile conferent-frontend/nginx.conf $user@$server`:~/conferent/conferent-frontend/
scp -i $keyfile docker-compose.prod.yml $user@$server`:~/conferent/
scp -i $keyfile -r nginx/ $user@$server`:~/conferent/

# 5. Docker 설치 확인 및 설치
Write-Host "🔍 Docker 설치 상태 확인 중..." -ForegroundColor Yellow
$dockerCheck = ssh -i $keyfile $user@$server "which docker-compose"
if (-not $dockerCheck) {
    Write-Host "🐳 Docker Compose가 설치되어 있지 않습니다. 설치 중..." -ForegroundColor Yellow
    scp -i $keyfile install-docker.sh $user@$server`:~/
    ssh -i $keyfile $user@$server "chmod +x ~/install-docker.sh && ~/install-docker.sh"
    Write-Host "✅ Docker 설치 완료! 잠시 후 재시도..." -ForegroundColor Green
    Start-Sleep -Seconds 5
}

# 6. 원격 서버에서 도커 실행
Write-Host "🐳 도커 컨테이너 실행 중..." -ForegroundColor Yellow
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml down"
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml up --build -d"

Write-Host "✅ 배포 완료!" -ForegroundColor Green
Write-Host "🌐 서비스 접속: http://$server" -ForegroundColor Cyan

# 7. 서비스 상태 확인
Write-Host "📊 서비스 상태 확인 중..." -ForegroundColor Yellow
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml ps" 