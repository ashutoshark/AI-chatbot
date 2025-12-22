# 🎉 Your Code is on GitHub!

**Repository**: https://github.com/ashutoshark/AI-chatbot

---

## 🚀 Quick Deploy from GitHub (15 minutes)

Your code is now on GitHub and ready to deploy! Follow these steps:

### Step 1: Deploy Backend to Render (7 minutes)

1. **Go to Render**: https://render.com/register
2. Sign up with GitHub
3. Click **"New +"** → **"PostgreSQL"**
   - Name: `chatbot-db`
   - Click "Create Database"
   - Copy the **External Database URL**

4. Click **"New +"** → **"Web Service"**
5. Connect your GitHub repository:
   - Select: **ashutoshark/AI-chatbot**
6. Configure:
   ```
   Name: chatbot-backend
   Region: Choose closest to you
   Branch: main
   Runtime: Java
   Root Directory: (leave empty)
   Build Command: cd backend && mvn clean package -DskipTests
   Start Command: java -Dserver.port=$PORT -jar backend/target/backend-1.0.0.jar
   Instance Type: Free
   ```

7. **Environment Variables** (click "Advanced"):
   ```
   LLM_API_KEY = your_groq_api_key_here
   DATABASE_URL = (paste PostgreSQL URL from step 3)
   DB_DRIVER = org.postgresql.Driver
   HIBERNATE_DIALECT = org.hibernate.dialect.PostgreSQLDialect
   SPRING_PROFILES_ACTIVE = production
   ```

8. Click **"Create Web Service"**
9. Wait 3-5 minutes for deployment
10. **Copy your backend URL**: `https://chatbot-backend-xxxx.onrender.com`

---

### Step 2: Deploy Frontend to Netlify (5 minutes)

#### Option A: GitHub Auto-Deploy (Recommended)

1. **Go to Netlify**: https://app.netlify.com/signup
2. Sign up with GitHub
3. Click **"Add new site"** → **"Import an existing project"**
4. Choose **"Deploy with GitHub"**
5. Select repository: **ashutoshark/AI-chatbot**
6. Configure:
   ```
   Base directory: frontend
   Build command: npm run build
   Publish directory: frontend/dist
   ```
7. **Environment variables**:
   ```
   VITE_API_BASE_URL = https://YOUR-BACKEND-URL.onrender.com/api
   ```
   (Replace with your Render URL from Step 1.10)
8. Click **"Deploy"**
9. Wait 2-3 minutes
10. **Copy your frontend URL**: `https://random-name.netlify.app`

#### Option B: Manual Deploy

1. Clone and build locally:
   ```bash
   git clone https://github.com/ashutoshark/AI-chatbot.git
   cd AI-chatbot/frontend
   echo VITE_API_BASE_URL=https://YOUR-BACKEND-URL.onrender.com/api > .env.production
   npm install
   npm run build
   ```
2. Go to https://app.netlify.com
3. Drag `frontend/dist` folder
4. Done!

---

### Step 3: Update CORS (2 minutes)

1. In your local code, edit `backend/src/main/java/com/chatbot/config/WebConfig.java`
2. Add your Netlify URL:
   ```java
   .allowedOrigins(
       "http://localhost:3000",
       "https://your-app.netlify.app",  // Your Netlify URL
       "https://*.netlify.app"
   )
   ```
3. Commit and push:
   ```bash
   git add .
   git commit -m "Update CORS for production"
   git push origin main
   ```
4. Render will auto-redeploy (or click "Manual Deploy")

---

### Step 4: Test! 🎉

Visit your Netlify URL and start chatting!

**First request may take 30-60 seconds (cold start is normal for free tier)**

---

## 📝 Your Deployment Info

Fill this in as you deploy:

**GitHub Repo**: https://github.com/ashutoshark/AI-chatbot ✅

**Backend URL**: `https://_____________________.onrender.com`

**Frontend URL**: `https://_____________________.netlify.app`

**Groq API Key**: Get from https://console.groq.com/keys

---

## 🔄 Auto-Deploy Setup

Both platforms now auto-deploy when you push to GitHub:

```bash
# Make changes
git add .
git commit -m "Your changes"
git push origin main

# Render and Netlify auto-deploy! 🚀
```

---

## 🆘 Troubleshooting

### Build fails on Render?
- Check logs in Render dashboard
- Verify environment variables are set
- Try "Clear build cache & deploy"

### Frontend can't connect?
- Update CORS in WebConfig.java with your Netlify domain
- Verify VITE_API_BASE_URL is correct
- Check browser console (F12) for errors

### Backend shows "Service Unavailable"?
- Free tier sleeps after 15 min of inactivity
- First request wakes it up (takes 30-60 seconds)
- Use UptimeRobot.com to keep it awake

---

## 💰 Cost

**$0/month** with these free tiers:
- Render: 750 hours/month
- Netlify: 100GB bandwidth
- PostgreSQL: 1GB storage
- Groq AI: Free tier

---

## 📚 More Help

- **Step-by-step**: [DEPLOY_INSTRUCTIONS.md](./DEPLOY_INSTRUCTIONS.md)
- **All options**: [DEPLOY_NOW.md](./DEPLOY_NOW.md)
- **Checklist**: [CHECKLIST.md](./CHECKLIST.md)

---

**Ready to deploy? Start with Step 1!** 🚀
