@echo off
echo ================================================
echo    Building for Production Deployment
echo ================================================
echo.

REM Backend
echo [1/2] Building Backend (Spring Boot)...
cd backend
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo X Backend build failed!
    pause
    exit /b 1
)
echo √ Backend built successfully: backend/target/backend-1.0.0.jar
cd ..

REM Frontend
echo.
echo [2/2] Building Frontend (React)...
cd frontend

REM Update API URL for production
set /p BACKEND_URL="Enter your backend URL (e.g., https://your-app.onrender.com): "
echo VITE_API_BASE_URL=%BACKEND_URL%/api > .env.production

call npm run build
if %errorlevel% neq 0 (
    echo X Frontend build failed!
    pause
    exit /b 1
)
echo √ Frontend built successfully: frontend/dist/
cd ..

echo.
echo ================================================
echo    Build Complete! Ready to Deploy
echo ================================================
echo.
echo Backend JAR:    backend/target/backend-1.0.0.jar
echo Frontend dist:  frontend/dist/
echo.
echo Next steps:
echo 1. Deploy backend JAR to Render
echo 2. Deploy frontend/dist folder to Netlify
echo.
echo See DEPLOY_NOW.md for detailed instructions
echo.
pause
