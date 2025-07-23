# EC2 Deployment Script (Windows PowerShell) - English Version
# Usage: .\deploy-en.ps1 -server "your-ec2-ip" -user "ubuntu" -keyfile "path/to/your-key.pem" [-skipBuild]

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

Write-Host "🚀 Starting Conferent deployment..." -ForegroundColor Green

if (-not $skipBuild) {
    # 1. Backend build
    Write-Host "📦 Building backend..." -ForegroundColor Yellow
    Set-Location conferent-backend
    if (Test-Path ".\gradlew.bat") {
        .\gradlew.bat clean build
    } else {
        Write-Host "⚠️ gradlew.bat not found. Creating gradle wrapper..." -ForegroundColor Yellow
        gradle wrapper
        .\gradlew.bat clean build
    }
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Backend build failed!" -ForegroundColor Red
        exit 1
    }
    Set-Location ..

    # 2. Frontend build
    Write-Host "📦 Building frontend..." -ForegroundColor Yellow
    Set-Location conferent-frontend
    npm install
    npm run build
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Frontend build failed!" -ForegroundColor Red
        exit 1
    }
    Set-Location ..
} else {
    Write-Host "⚡ Skipping build - Using existing build files" -ForegroundColor Cyan
}

# 3. Check build files
Write-Host "📁 Checking build files..." -ForegroundColor Yellow
if (-not (Test-Path "conferent-backend/build/libs/*.jar")) {
    Write-Host "❌ Backend JAR file not found. Please build first!" -ForegroundColor Red
    exit 1
}
if (-not (Test-Path "conferent-frontend/dist/index.html")) {
    Write-Host "❌ Frontend dist folder not found. Please build first!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ All build files confirmed" -ForegroundColor Green

# 4. Transfer files via SCP
Write-Host "🔄 Transferring files to EC2..." -ForegroundColor Yellow

# Create remote directories
ssh -i $keyfile $user@$server "mkdir -p ~/conferent/conferent-backend/build/libs"
ssh -i $keyfile $user@$server "mkdir -p ~/conferent/conferent-frontend"
ssh -i $keyfile $user@$server "mkdir -p ~/conferent/nginx"

# Transfer files
scp -i $keyfile -r conferent-backend/build/libs/*.jar $user@$server`:~/conferent/conferent-backend/build/libs/
scp -i $keyfile conferent-backend/Dockerfile.prod $user@$server`:~/conferent/conferent-backend/
scp -i $keyfile -r conferent-frontend/dist $user@$server`:~/conferent/conferent-frontend/
scp -i $keyfile conferent-frontend/Dockerfile.prod $user@$server`:~/conferent/conferent-frontend/
scp -i $keyfile conferent-frontend/nginx.conf $user@$server`:~/conferent/conferent-frontend/
scp -i $keyfile docker-compose.prod.yml $user@$server`:~/conferent/
scp -i $keyfile -r nginx/ $user@$server`:~/conferent/

# 5. Check and install Docker
Write-Host "🔍 Checking Docker installation..." -ForegroundColor Yellow
$dockerCheck = ssh -i $keyfile $user@$server "which docker-compose"
if (-not $dockerCheck) {
    Write-Host "🐳 Docker Compose not found. Installing..." -ForegroundColor Yellow
    scp -i $keyfile install-docker.sh $user@$server`:~/
    ssh -i $keyfile $user@$server "chmod +x ~/install-docker.sh && ~/install-docker.sh"
    Write-Host "✅ Docker installation complete! Retrying in a moment..." -ForegroundColor Green
    Start-Sleep -Seconds 5
}

# 6. Run Docker containers
Write-Host "🐳 Starting Docker containers..." -ForegroundColor Yellow
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml down"
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml up --build -d"

Write-Host "✅ Deployment complete!" -ForegroundColor Green
Write-Host "🌐 Service URL: http://$server" -ForegroundColor Cyan

# 7. Check service status
Write-Host "📊 Checking service status..." -ForegroundColor Yellow
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml ps" 