@echo off
echo ========================================
echo   AI Chatbot - Starting Application
echo ========================================
echo.

echo [1/3] Checking prerequisites...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Please install Java 17+
    pause
    exit /b 1
)

node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Node.js not found. Please install Node.js 20+
    pause
    exit /b 1
)

echo [2/3] Starting Backend (Spring Boot)...
start "AI Chatbot Backend" cmd /k "cd backend && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo [3/3] Starting Frontend (React)...
start "AI Chatbot Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo ========================================
echo   Application Starting!
echo ========================================
echo.
echo Backend:  http://localhost:8081
echo Frontend: http://localhost:3000
echo.
echo Press any key to open browser...
pause >nul

start http://localhost:3000

echo.
echo Application is running!
echo Close the terminal windows to stop the servers.
echo.
pause
