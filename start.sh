#!/bin/bash

echo "========================================"
echo "  AI Chatbot - Starting Application"
echo "========================================"
echo

echo "[1/3] Checking prerequisites..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java not found. Please install Java 17+"
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "ERROR: Node.js not found. Please install Node.js 20+"
    exit 1
fi

echo "[2/3] Starting Backend (Spring Boot)..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!
cd ..

echo "[3/3] Starting Frontend (React)..."
sleep 5
cd frontend
npm run dev &
FRONTEND_PID=$!
cd ..

echo
echo "========================================"
echo "  Application Started!"
echo "========================================"
echo
echo "Backend:  http://localhost:8081"
echo "Frontend: http://localhost:3000"
echo
echo "Press Ctrl+C to stop all servers..."
echo

# Trap Ctrl+C and cleanup
trap "kill $BACKEND_PID $FRONTEND_PID; echo 'Servers stopped'; exit" INT

# Wait for user interrupt
wait
