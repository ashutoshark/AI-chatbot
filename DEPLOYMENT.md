# AI Chatbot - Deployment Guide

Complete guide for deploying your AI chatbot application to production.

## 📋 Prerequisites

- Node.js 20.18+ (frontend)
- Java 17+ (backend)
- MySQL 8.0+ (database)
- Groq API key (get free at https://console.groq.com/keys)

## 🔧 Backend Deployment

### 1. Configure Production Settings

Update `backend/src/main/resources/application.yml`:

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://YOUR_DB_HOST:3306/chatbot_db
    username: YOUR_DB_USER
    password: YOUR_DB_PASSWORD
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

llm:
  provider: groq
  api:
    key: ${LLM_API_KEY}
  groq:
    model: llama-3.1-8b-instant
    base-url: https://api.groq.com/openai/v1
  max-tokens: 500
```

### 2. Build Backend

```bash
cd backend
mvn clean package -DskipTests
```

This creates `backend/target/backend-1.0.0.jar`

### 3. Run Backend

```bash
# Set API key as environment variable
export LLM_API_KEY=your_groq_api_key

# Run the JAR
java -jar backend/target/backend-1.0.0.jar
```

Or with inline environment:

```bash
LLM_API_KEY=your_key java -jar backend/target/backend-1.0.0.jar
```

### 4. Backend Deployment Options

#### Option A: Traditional Server (Linux/Windows)
- Copy JAR to server
- Install Java 17+
- Run as systemd service (Linux) or Windows Service
- Use nginx/Apache as reverse proxy

#### Option B: Docker
```dockerfile
FROM openjdk:17-slim
COPY backend/target/backend-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t ai-chatbot-backend .
docker run -p 8081:8081 -e LLM_API_KEY=your_key ai-chatbot-backend
```

#### Option C: Cloud Platforms
- **AWS Elastic Beanstalk**: Deploy JAR directly
- **Heroku**: Use Procfile with `web: java -jar backend/target/backend-1.0.0.jar`
- **Azure App Service**: Deploy as Java application
- **Google Cloud Run**: Deploy containerized version

## 🎨 Frontend Deployment

### 1. Configure API URL

Update `frontend/.env.production`:

```
VITE_API_BASE_URL=https://your-backend-url.com/api
```

### 2. Build Frontend

```bash
cd frontend
npm install
npm run build
```

This creates `frontend/dist` folder with production-ready static files.

### 3. Frontend Deployment Options

#### Option A: Netlify (Recommended - Free)
1. Install Netlify CLI: `npm install -g netlify-cli`
2. Deploy: `cd frontend && netlify deploy --prod --dir=dist`
3. Set environment variable `VITE_API_BASE_URL` in Netlify dashboard
4. Add `_redirects` file in `dist/`:
   ```
   /*    /index.html   200
   ```

#### Option B: Vercel (Free)
1. Install Vercel CLI: `npm install -g vercel`
2. Deploy: `cd frontend && vercel --prod`
3. Set environment variable in Vercel dashboard

#### Option C: AWS S3 + CloudFront
```bash
# Install AWS CLI
aws s3 sync frontend/dist s3://your-bucket-name --delete
aws cloudfront create-invalidation --distribution-id YOUR_DIST_ID --paths "/*"
```

#### Option D: Traditional Web Server (nginx)
```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /var/www/chatbot/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8081;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

#### Option E: Docker
```dockerfile
FROM nginx:alpine
COPY frontend/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## 🗄️ Database Setup

### MySQL Production Configuration

```sql
-- Create database
CREATE DATABASE chatbot_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'chatbot_user'@'%' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON chatbot_db.* TO 'chatbot_user'@'%';
FLUSH PRIVILEGES;
```

### Cloud Database Options
- **AWS RDS** (MySQL)
- **Azure Database for MySQL**
- **Google Cloud SQL**
- **PlanetScale** (free tier)
- **Railway** (free tier)

## 🚀 Full Stack Deployment (Docker Compose)

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: chatbot_db
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"

  backend:
    build: ./backend
    ports:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/chatbot_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: rootpassword
      LLM_API_KEY: ${LLM_API_KEY}
    depends_on:
      - mysql

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

Run: `LLM_API_KEY=your_key docker-compose up -d`

## 🔒 Security Checklist

- [ ] Change default database passwords
- [ ] Store API keys in environment variables (never commit)
- [ ] Enable HTTPS (use Let's Encrypt for free SSL)
- [ ] Set up CORS properly in backend
- [ ] Enable rate limiting
- [ ] Use firewall rules to restrict database access
- [ ] Regular database backups
- [ ] Monitor API usage and costs

## 📊 Monitoring

- Backend logs: `tail -f logs/spring.log`
- Frontend analytics: Google Analytics, Plausible
- Application monitoring: New Relic, Datadog
- Uptime monitoring: UptimeRobot, Pingdom

## 💰 Cost Estimates (Monthly)

**Free Tier Option:**
- Frontend: Netlify/Vercel (Free)
- Backend: Railway/Render (Free tier)
- Database: PlanetScale (Free tier)
- API: Groq (Free tier)
- **Total: $0/month** (with usage limits)

**Production Option:**
- Frontend: Netlify Pro ($19)
- Backend: AWS EC2 t3.small ($15)
- Database: AWS RDS db.t3.micro ($15)
- API: Groq (Free)
- **Total: ~$50/month**

## 🎯 Quick Start Commands

### Local Development
```bash
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend
npm run dev
```

### Production Build
```bash
# Backend
cd backend && mvn clean package

# Frontend
cd frontend && npm run build

# Deploy dist folder and JAR file
```

## 🆘 Troubleshooting

### Backend won't start
- Check MySQL is running and accessible
- Verify API key is set: `echo $LLM_API_KEY`
- Check logs: `java -jar backend.jar --debug`

### Frontend can't connect to backend
- Check CORS settings in backend WebConfig
- Verify API URL in `.env.production`
- Check browser console for errors

### Database connection fails
- Test connection: `mysql -h host -u user -p`
- Check firewall rules
- Verify credentials in application.yml

## 📝 Environment Variables Summary

**Backend:**
- `LLM_API_KEY` - Groq API key (required)
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database user
- `SPRING_DATASOURCE_PASSWORD` - Database password

**Frontend:**
- `VITE_API_BASE_URL` - Backend API URL

---

**Your application is now deployment-ready!** 🎉

Choose your preferred deployment method above and follow the steps. For beginners, we recommend starting with Netlify (frontend) + Railway (backend) for a completely free deployment.
