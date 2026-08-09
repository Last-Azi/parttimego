@echo off

echo ==============================
echo   PartTimeGo Docker Deploy
echo ==============================

REM 1. Build backend
echo [1/4] Building backend...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

REM 2. Stop old containers
echo [2/4] Stopping old containers...
docker-compose down

REM 3. Build and start
echo [3/4] Building and starting containers...
docker-compose up -d --build

if %errorlevel% neq 0 (
    echo Start failed!
    pause
    exit /b 1
)

REM 4. Wait
echo [4/4] Waiting for services...
timeout /t 10 /nobreak

echo.
echo ==============================
echo   Deploy Complete!
echo ==============================
echo.
echo Frontend: http://localhost
echo Backend:  http://localhost:9090
echo.
echo Logs:     docker-compose logs -f
echo Stop:     docker-compose down
echo.
pause
