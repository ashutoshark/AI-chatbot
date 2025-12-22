#!/bin/bash

echo "================================================"
echo "   Building for Production Deployment"
echo "================================================"
echo

# Backend
echo "[1/2] Building Backend (Spring Boot)..."
cd backend
mvn clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "✓ Backend built successfully: backend/target/backend-1.0.0.jar"
else
    echo "✗ Backend build failed!"
    exit 1
fi
cd ..

# Frontend
echo
echo "[2/2] Building Frontend (React)..."
cd frontend

# Update API URL for production
read -p "Enter your backend URL (e.g., https://your-app.onrender.com): " BACKEND_URL
echo "VITE_API_BASE_URL=${BACKEND_URL}/api" > .env.production

npm run build
if [ $? -eq 0 ]; then
    echo "✓ Frontend built successfully: frontend/dist/"
else
    echo "✗ Frontend build failed!"
    exit 1
fi
cd ..

echo
echo "================================================"
echo "   Build Complete! Ready to Deploy"
echo "================================================"
echo
echo "Backend JAR:    backend/target/backend-1.0.0.jar"
echo "Frontend dist:  frontend/dist/"
echo
echo "Next steps:"
echo "1. Deploy backend JAR to Render"
echo "2. Deploy frontend/dist folder to Netlify"
echo
echo "See DEPLOY_NOW.md for detailed instructions"
