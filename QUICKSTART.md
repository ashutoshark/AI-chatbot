# AI Chatbot - Quick Reference

## Start Application (Windows)
```bash
start.bat
```

## Start Application (Linux/Mac)
```bash
chmod +x start.sh
./start.sh
```

## Manual Start

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm run dev
```

## URLs
- Frontend: http://localhost:3000
- Backend: http://localhost:8081
- API Docs: http://localhost:8081/api

## Environment Setup

### Backend Configuration
File: `backend/src/main/resources/application.yml`
```yaml
llm:
  api:
    key: YOUR_GROQ_API_KEY
```

### Frontend Configuration
File: `frontend/.env`
```
VITE_API_BASE_URL=http://localhost:8081/api
```

## Common Commands

### Build Backend
```bash
cd backend
mvn clean package
```

### Build Frontend
```bash
cd frontend
npm run build
```

### Run Tests (Backend)
```bash
cd backend
mvn test
```

### Production Build
```bash
# Backend
cd backend && mvn clean package

# Frontend  
cd frontend && npm run build
```

## API Testing

### Send Message
```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello"}'
```

### Create Conversation
```bash
curl -X POST http://localhost:8081/api/conversations
```

### Get Messages
```bash
curl http://localhost:8081/api/conversations/{id}/messages
```

## Troubleshooting

### Port Already in Use
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <process_id> /F

# Linux/Mac
lsof -ti:8081 | xargs kill -9
```

### Clear Maven Cache
```bash
mvn clean
rm -rf ~/.m2/repository
```

### Clear npm Cache
```bash
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

## Database Commands

### Create Database
```sql
CREATE DATABASE chatbot_db;
```

### View Tables
```sql
USE chatbot_db;
SHOW TABLES;
```

### View Messages
```sql
SELECT * FROM messages ORDER BY created_at DESC LIMIT 10;
```

### Clear All Data
```sql
TRUNCATE TABLE messages;
TRUNCATE TABLE conversations;
```

## Deployment

See [DEPLOYMENT.md](./DEPLOYMENT.md) for complete deployment guide.

Quick options:
- **Frontend**: Netlify, Vercel, AWS S3
- **Backend**: Railway, Render, AWS Elastic Beanstalk
- **Database**: AWS RDS, PlanetScale, Railway
