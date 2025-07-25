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

Write-Host "[START] Starting Conferent deployment..." -ForegroundColor Green

# IP 주소 자동 해석 (도메인인 경우)
if ($server -match "^[a-zA-Z]") {
    Write-Host "[RESOLVE] Resolving domain to IP address..." -ForegroundColor Yellow
    try {
        $resolvedIP = (Resolve-DnsName $server -Type A | Where-Object { $_.Type -eq "A" } | Select-Object -First 1).IPAddress
        if ($resolvedIP) {
            Write-Host "[SUCCESS] Resolved $server -> $resolvedIP" -ForegroundColor Green
            $server = $resolvedIP
        }
    } catch {
        Write-Host "[WARNING] Could not resolve domain, using as-is" -ForegroundColor Yellow
    }
}

if (-not $skipBuild) {
    # Gradle cache cleanup and directory creation (fix permission issues)
    Write-Host "[CLEAN] Cleaning Gradle cache to fix permission issues..." -ForegroundColor Yellow
    try {
        # Delete entire Gradle home directory
        $gradleHome = "$env:USERPROFILE\.gradle"
        if (Test-Path $gradleHome) {
            Remove-Item -Recurse -Force $gradleHome -ErrorAction SilentlyContinue
        }
        
        # Create new directories
        New-Item -ItemType Directory -Path $gradleHome -Force | Out-Null
        New-Item -ItemType Directory -Path "$gradleHome\wrapper" -Force | Out-Null
        New-Item -ItemType Directory -Path "$gradleHome\wrapper\dists" -Force | Out-Null
        New-Item -ItemType Directory -Path "$gradleHome\caches" -Force | Out-Null
        
        # Set permissions
        $acl = Get-Acl $gradleHome
        $accessRule = New-Object System.Security.AccessControl.FileSystemAccessRule($env:USERNAME, "FullControl", "ContainerInherit,ObjectInherit", "None", "Allow")
        $acl.SetAccessRule($accessRule)
        Set-Acl -Path $gradleHome -AclObject $acl
        
        Write-Host "[SUCCESS] Gradle directories recreated with proper permissions" -ForegroundColor Green
    } catch {
        Write-Host "[WARNING] Could not fix permissions, trying alternative method..." -ForegroundColor Yellow
    }

    # 1. Backend build
    Write-Host "[BUILD] Building backend..." -ForegroundColor Yellow
    Set-Location conferent-backend
    
    # Gradle wrapper execution with permission handling
    if (Test-Path ".\gradlew.bat") {
        Write-Host "[TOOL] Using existing Gradle wrapper..." -ForegroundColor Cyan
        # Multiple attempts to resolve permission issues
        $buildSuccess = $false
        for ($i = 1; $i -le 3; $i++) {
            Write-Host "[ATTEMPT] Build attempt $i/3..." -ForegroundColor Yellow
            .\gradlew.bat clean build --no-daemon --refresh-dependencies
            if ($LASTEXITCODE -eq 0) {
                $buildSuccess = $true
                break
            } else {
                Write-Host "[WARNING] Build attempt $i failed, retrying..." -ForegroundColor Yellow
                Start-Sleep -Seconds 2
            }
        }
        if (-not $buildSuccess) {
            Write-Host "[WARNING] Gradle wrapper failed, trying system Gradle..." -ForegroundColor Yellow
            
            # Check and use system Gradle
            $systemGradle = Get-Command gradle -ErrorAction SilentlyContinue
            if ($systemGradle) {
                Write-Host "[TOOL] Using system Gradle at: $($systemGradle.Source)" -ForegroundColor Cyan
                gradle clean build --no-daemon
                if ($LASTEXITCODE -eq 0) {
                    $buildSuccess = $true
                    Write-Host "[SUCCESS] Backend build successful with system Gradle!" -ForegroundColor Green
                }
            }
            
            if (-not $buildSuccess) {
                Write-Host "[ERROR] Backend build failed with both wrapper and system Gradle!" -ForegroundColor Red
                Write-Host "[INFO] Please try running PowerShell as Administrator or install Gradle manually" -ForegroundColor Yellow
                Set-Location ..
                exit 1
            }
        }
    } else {
        Write-Host "[WARNING] gradlew.bat not found. Creating gradle wrapper..." -ForegroundColor Yellow
        gradle wrapper
        .\gradlew.bat clean build --no-daemon
        if ($LASTEXITCODE -ne 0) {
            Write-Host "[ERROR] Backend build failed!" -ForegroundColor Red
            Set-Location ..
            exit 1
        }
    }
    Write-Host "[SUCCESS] Backend build successful!" -ForegroundColor Green
    Set-Location ..

    # 2. Frontend build
    Write-Host "[BUILD] Building frontend..." -ForegroundColor Yellow
    Set-Location conferent-frontend
    
    # Check Node.js and npm
    $nodeVersion = node --version 2>$null
    if (-not $nodeVersion) {
        Write-Host "[ERROR] Node.js not found! Please install Node.js first." -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Write-Host "[SUCCESS] Using Node.js $nodeVersion" -ForegroundColor Green
    
    # Clean cache and install
    Write-Host "[CLEAN] Cleaning npm cache..." -ForegroundColor Yellow
    npm cache clean --force 2>$null
    
    Write-Host "[INSTALL] Installing dependencies..." -ForegroundColor Yellow
    npm install --no-fund --no-audit
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] npm install failed!" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    
    Write-Host "[BUILD] Building frontend..." -ForegroundColor Yellow
    npm run build
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Frontend build failed!" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Write-Host "[SUCCESS] Frontend build successful!" -ForegroundColor Green
    Set-Location ..
} else {
    Write-Host "[SKIP] Skipping build - Using existing build files" -ForegroundColor Cyan
}

# 3. Check build files
Write-Host "[CHECK] Checking build files..." -ForegroundColor Yellow
if (-not (Test-Path "conferent-backend/build/libs/*.jar")) {
    Write-Host "[ERROR] Backend JAR file not found. Please build first!" -ForegroundColor Red
    exit 1
}
if (-not (Test-Path "conferent-frontend/dist/index.html")) {
    Write-Host "[ERROR] Frontend dist folder not found. Please build first!" -ForegroundColor Red
    exit 1
}
Write-Host "[SUCCESS] All build files confirmed" -ForegroundColor Green

# 4. Transfer files via SCP
Write-Host "[TRANSFER] Transferring files to EC2..." -ForegroundColor Yellow

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
Write-Host "[CHECK] Checking Docker installation..." -ForegroundColor Yellow
$dockerCheck = ssh -i $keyfile $user@$server "which docker-compose"
if (-not $dockerCheck) {
    Write-Host "[INSTALL] Docker Compose not found. Installing..." -ForegroundColor Yellow
    scp -i $keyfile install-docker.sh $user@$server`:~/
    ssh -i $keyfile $user@$server "chmod +x ~/install-docker.sh && ~/install-docker.sh"
    Write-Host "[SUCCESS] Docker installation complete! Retrying in a moment..." -ForegroundColor Green
    Start-Sleep -Seconds 5
}

# 6. Run Docker containers
Write-Host "[RUN] Starting Docker containers..." -ForegroundColor Yellow
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml down"
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml up --build -d"

Write-Host "[COMPLETE] Deployment complete!" -ForegroundColor Green
Write-Host "[ACCESS] Service URL: https://conferent.duckdns.org" -ForegroundColor Cyan
Write-Host "[INFO] Note: EC2 IP($server) only supports HTTP access." -ForegroundColor Yellow

# 7. Check service status
Write-Host "[STATUS] Checking service status..." -ForegroundColor Yellow
ssh -i $keyfile $user@$server "cd ~/conferent && sudo docker-compose -f docker-compose.prod.yml ps" 