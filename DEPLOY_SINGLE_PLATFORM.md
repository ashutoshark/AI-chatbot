# 🎯 Deploy Everything on Render (ONE Platform!)

**Repository**: https://github.com/ashutoshark/AI-chatbot

Deploy both backend AND frontend on **Render.com** - No need for multiple platforms!

---

## 🚀 Complete Deployment (20 minutes, $0)

### Step 1: Create Render Account (2 minutes)

1. Go to https://render.com/register
2. Click **"Sign up with GitHub"**
3. Authorize Render to access your repositories

---

### Step 2: Deploy Database (3 minutes)

1. In Render dashboard, click **"New +"** → **"PostgreSQL"**
2. Configure:
   ```
   Name: chatbot-db
   Database: chatbot_db
   User: chatbot_user
   Region: Choose closest to you (e.g., Oregon, Frankfurt)
   Plan: Free
   ```
3. Click **"Create Database"**
4. Wait 1-2 minutes for creation
5. Go to database page → **Copy "External Database URL"** (keep this for Step 3)

**Example URL**: 
```
postgres://chatbot_user:password@dpg-xxxxx.oregon-postgres.render.com/chatbot_db
```

---

### Step 3: Deploy Backend (7 minutes)

1. Click **"New +"** → **"Web Service"**
2. Click **"Connect GitHub"** → Select **"ashutoshark/AI-chatbot"**
3. Configure:
   ```
   Name: chatbot-backend
   Region: Same as database (Oregon, Frankfurt, etc.)
   Branch: main
   Runtime: Java
   Root Directory: (leave empty)
   Build Command: cd backend && mvn clean package -DskipTests
   Start Command: java -Dserver.port=$PORT -jar backend/target/backend-1.0.0.jar
   Instance Type: Free
   ```

4. Click **"Advanced"** and add **Environment Variables**:
   ```
   Key: LLM_API_KEY
   Value: your_groq_api_key_here
   
   Key: DATABASE_URL
   Value: (paste PostgreSQL URL from Step 2.5)
   
   Key: DB_DRIVER
   Value: org.postgresql.Driver
   
   Key: HIBERNATE_DIALECT
   Value: org.hibernate.dialect.PostgreSQLDialect
   
   Key: SPRING_PROFILES_ACTIVE
   Value: production
   ```
   
   **Get Groq API Key**: https://console.groq.com/keys

5. Click **"Create Web Service"**
6. Wait 5-7 minutes (watch build logs)
7. Once deployed, **copy your backend URL**: 
   ```
   https://chatbot-backend-xxxx.onrender.com
   ```

**Test Backend**: Click the URL and add `/actuator/health` - should see `{"status":"UP"}`

---

### Step 4: Deploy Frontend (5 minutes)

1. Click **"New +"** → **"Static Site"**
2. Select repository: **"ashutoshark/AI-chatbot"**
3. Configure:
   ```
   Name: chatbot-frontend
   Branch: main
   Root Directory: frontend
   Build Command: npm install && npm run build
   Publish Directory: dist
   ```

4. Click **"Advanced"** and add **Environment Variable**:
   ```
   Key: VITE_API_BASE_URL
   Value: https://chatbot-backend-xxxx.onrender.com/api
   ```
   ⚠️ Replace with YOUR backend URL from Step 3.7 + `/api`

5. Click **"Create Static Site"**
6. Wait 3-4 minutes
7. **Copy your frontend URL**: 
   ```
   https://chatbot-frontend.onrender.com
   ```

---

### Step 5: Update CORS (3 minutes)

1. In your local code, open `backend/src/main/java/com/chatbot/config/WebConfig.java`

2. Add your Render frontend URL:
   ```java
   @Override
   public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/api/**")
           .allowedOrigins(
               "http://localhost:3000",
               "https://chatbot-frontend.onrender.com",  // ADD THIS
               "https://*.onrender.com"                   // ADD THIS
           )
           .allowedMethods("GET", "POST", "PUT", "DELETE")
           .allowedHeaders("*")
           .allowCredentials(true);
   }
   ```

3. Save and push to GitHub:
   ```bash
   git add backend/src/main/java/com/chatbot/config/WebConfig.java
   git commit -m "Update CORS for Render frontend"
   git push origin main
   ```

4. Render will **auto-deploy** backend in 3-5 minutes (or click "Manual Deploy")

---

### Step 6: Test Your App! 🎉

1. Open your frontend URL: `https://chatbot-frontend.onrender.com`
2. Type a message: "Hello, how are you?"
3. Wait 30-60 seconds for first response (cold start is normal)
4. Chat away! 🤖

---

## 📝 Your Deployment Summary

**GitHub**: https://github.com/ashutoshark/AI-chatbot ✅

**Platform**: Render.com (everything in one place!)

**Database**: `https://dashboard.render.com/d/dpg-_______` 

**Backend**: `https://chatbot-backend-_____.onrender.com`

**Frontend**: `https://chatbot-frontend.onrender.com`

**Groq API**: https://console.groq.com/keys

---

## 🔄 Auto-Deploy Workflow

Every time you push to GitHub, Render auto-deploys:

```bash
# Make changes to your code
git add .
git commit -m "Add new feature"
git push origin main

# ✨ Render automatically deploys:
# - Backend rebuilds and restarts
# - Frontend rebuilds and updates
# - Takes 3-5 minutes
```

Watch deployments at: https://dashboard.render.com

---

## 🛠️ Managing Your App

### View Logs
- **Backend**: Dashboard → chatbot-backend → Logs
- **Frontend**: Dashboard → chatbot-frontend → Logs
- **Database**: Dashboard → chatbot-db → Info

### Redeploy Manually
- Go to service → Click **"Manual Deploy"** → Select "main" branch

### Update Environment Variables
- Go to service → Environment → Edit variables → Save Changes
- Service will auto-redeploy

### Monitor Database
- Dashboard → chatbot-db → **"Connect"** for connection string
- Use pgAdmin or DBeaver to connect

---

## ⚡ Keep Backend Awake (Optional)

Free tier sleeps after 15 minutes of inactivity. To prevent this:

### Option 1: UptimeRobot (Recommended)
1. Go to https://uptimerobot.com (free)
2. Add New Monitor:
   ```
   Monitor Type: HTTP(s)
   Friendly Name: Chatbot Backend
   URL: https://chatbot-backend-xxxx.onrender.com/actuator/health
   Monitoring Interval: 5 minutes
   ```
3. Backend stays awake 24/7!

### Option 2: Cron-job.org
1. Go to https://cron-job.org (free)
2. Create new job to ping your backend every 5 minutes

---

## 🆘 Troubleshooting

### Backend Build Fails?
1. Check logs: Dashboard → chatbot-backend → Logs
2. Common issues:
   - Maven not found: Render should auto-detect Java
   - Port error: Render uses `$PORT` environment variable (already configured)
3. Try: Clear build cache and redeploy

### Frontend Shows 404?
1. Check build command in Render dashboard
2. Verify `Publish Directory` is set to `dist`
3. Check logs for build errors

### "Failed to fetch" Error?
1. Backend is sleeping (first request takes 60 sec)
2. Check CORS is updated with frontend URL
3. Verify `VITE_API_BASE_URL` has `/api` at the end

### Database Connection Fails?
1. Verify `DATABASE_URL` in backend environment variables
2. Check database status: Dashboard → chatbot-db
3. Ensure `SPRING_PROFILES_ACTIVE=production`

---

## 💰 Cost Breakdown

**Everything is FREE!** 🎉

| Service | Free Tier |
|---------|-----------|
| PostgreSQL | 1 GB storage, 90 days |
| Backend (Web Service) | 750 hours/month |
| Frontend (Static Site) | 100 GB bandwidth |
| Groq AI | Free tier with limits |

**Total Monthly Cost: $0**

**Limitations**:
- Backend sleeps after 15 min inactivity (use UptimeRobot)
- First request after sleep: 30-60 seconds
- Database deleted after 90 days of free plan (backup data!)

---

## 🎓 What You've Deployed

```
┌─────────────────────────────────────┐
│   Render.com (Single Platform)     │
├─────────────────────────────────────┤
│                                     │
│  📱 Frontend (Static Site)          │
│     - React + TypeScript UI         │
│     - Auto-deploy from GitHub       │
│     - https://chatbot-frontend...   │
│                                     │
│  ⚙️  Backend (Web Service)          │
│     - Spring Boot REST API          │
│     - Groq AI integration           │
│     - Auto-deploy from GitHub       │
│     - https://chatbot-backend...    │
│                                     │
│  💾 PostgreSQL Database             │
│     - Conversations & Messages      │
│     - Persistent storage            │
│                                     │
└─────────────────────────────────────┘
```

---

## 🎯 Quick Reference

### URLs to Remember
- **Render Dashboard**: https://dashboard.render.com
- **Your Frontend**: https://chatbot-frontend.onrender.com
- **Your Backend**: https://chatbot-backend-xxxx.onrender.com
- **GitHub Repo**: https://github.com/ashutoshark/AI-chatbot
- **Groq Console**: https://console.groq.com

### Commands
```bash
# Local development
cd backend && mvn spring-boot:run
cd frontend && npm run dev

# Deploy changes
git add .
git commit -m "Your changes"
git push origin main

# View logs
render logs chatbot-backend
render logs chatbot-frontend
```

---

## 🚀 Next Steps

1. **Share Your App**: Send the frontend URL to friends!
2. **Monitor Usage**: Check Groq API usage at console.groq.com
3. **Backup Database**: Export data regularly (free tier = 90 days)
4. **Add Features**: Push to GitHub, auto-deploys!
5. **Custom Domain**: Render allows custom domains (even on free tier!)

---

## ✅ Deployment Checklist

- [ ] Render account created
- [ ] PostgreSQL database deployed
- [ ] Backend deployed with environment variables
- [ ] Frontend deployed with backend URL
- [ ] CORS updated in WebConfig.java
- [ ] Tested: Send message and get AI response
- [ ] (Optional) UptimeRobot monitoring setup
- [ ] Frontend URL bookmarked
- [ ] Database connection string saved

---

**All done on ONE platform! 🎉**

Need help? Check [DEPLOY_INSTRUCTIONS.md](./DEPLOY_INSTRUCTIONS.md) or [DEPLOY_NOW.md](./DEPLOY_NOW.md)
