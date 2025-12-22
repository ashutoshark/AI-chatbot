# Quick Deploy Guide - Free Platforms 🚀

## Option 1: Render (Backend) + Netlify (Frontend) - RECOMMENDED ✨

This is the **easiest and completely FREE** option.

### Step 1: Deploy Backend to Render (5 minutes)

1. **Create Render Account**
   - Go to https://render.com
   - Sign up with GitHub (recommended) or email

2. **Create PostgreSQL Database** (Free tier includes database!)
   - Click "New +" → "PostgreSQL"
   - Name: `chatbot-db`
   - Database: `chatbot_db`
   - User: `chatbot_user`
   - Click "Create Database"
   - **Copy the External Database URL** (you'll need it)

3. **Deploy Backend**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository (or upload files)
   - Configure:
     - **Name**: `chatbot-backend`
     - **Region**: Choose closest to you
     - **Branch**: `main`
     - **Runtime**: `Java`
     - **Build Command**: `cd backend && mvn clean package -DskipTests`
     - **Start Command**: `java -jar backend/target/backend-1.0.0.jar`
   
4. **Set Environment Variables**
   Click "Environment" tab:
   ```
   LLM_API_KEY=your_groq_api_key_here
   DATABASE_URL=<paste your PostgreSQL URL from step 2>
   SPRING_DATASOURCE_URL=${DATABASE_URL}
   ```

5. **Update application.yml for PostgreSQL**
   
   Backend will use PostgreSQL instead of MySQL. Update `backend/src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/chatbot_db}
       username: ${DB_USERNAME:postgres}
       password: ${DB_PASSWORD:}
     jpa:
       hibernate:
         ddl-auto: update
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
   ```

6. **Add PostgreSQL dependency to pom.xml**:
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

7. **Click "Create Web Service"**
   - Render will build and deploy (takes 3-5 minutes)
   - Your backend URL: `https://chatbot-backend-xxxx.onrender.com`
   - **Save this URL!**

### Step 2: Deploy Frontend to Netlify (3 minutes)

**Option A: Drag & Drop (Easiest)**

1. **Build Frontend Locally**
   ```bash
   cd frontend
   
   # Update .env.production with your Render backend URL
   echo VITE_API_BASE_URL=https://chatbot-backend-xxxx.onrender.com/api > .env.production
   
   npm run build
   ```

2. **Deploy to Netlify**
   - Go to https://app.netlify.com
   - Click "Add new site" → "Deploy manually"
   - Drag the `frontend/dist` folder into the upload area
   - Done! Your site is live at `https://random-name.netlify.app`

**Option B: GitHub Auto-Deploy**

1. Push code to GitHub
2. Go to https://app.netlify.com
3. Click "Add new site" → "Import from Git"
4. Select your repository
5. Configure:
   - **Base directory**: `frontend`
   - **Build command**: `npm run build`
   - **Publish directory**: `frontend/dist`
6. Add environment variable:
   - Key: `VITE_API_BASE_URL`
   - Value: `https://chatbot-backend-xxxx.onrender.com/api`
7. Click "Deploy"

### Step 3: Test Your Live Application 🎉

Visit your Netlify URL and start chatting!

---

## Option 2: Vercel (Frontend) + Render (Backend)

### Backend: Same as Option 1 above

### Frontend on Vercel:

1. Go to https://vercel.com
2. Click "Add New" → "Project"
3. Import your GitHub repo
4. Configure:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
5. Add Environment Variable:
   ```
   VITE_API_BASE_URL=https://chatbot-backend-xxxx.onrender.com/api
   ```
6. Click "Deploy"

Done! Your app is at `https://your-app.vercel.app`

---

## Option 3: Railway (Backend + Database) + Netlify/Vercel (Frontend)

1. **Go to https://railway.app**
2. Sign up with GitHub
3. Click "New Project" → "Deploy from GitHub repo"
4. Select your repository
5. Railway auto-detects Java and builds
6. Add PostgreSQL:
   - Click "+ New"
   - Select "Database" → "PostgreSQL"
   - Railway auto-connects it
7. Add environment variable:
   ```
   LLM_API_KEY=your_groq_api_key_here
   ```
8. Your backend URL: `https://your-app.up.railway.app`
9. Deploy frontend to Netlify/Vercel using this URL

---

## Quick Checklist ✅

Before deploying:
- [ ] Backend builds successfully: `mvn clean package`
- [ ] Frontend builds successfully: `npm run build`
- [ ] You have your Groq API key
- [ ] PostgreSQL driver added to pom.xml (for Render/Railway)

After deploying:
- [ ] Backend health check passes
- [ ] Frontend loads without errors
- [ ] Chat functionality works
- [ ] Messages persist in database

---

## Important Notes 💡

### Free Tier Limitations:
- **Render**: 
  - Web service sleeps after 15 min of inactivity
  - First request after sleep takes 30-60 seconds (cold start)
  - 750 hours/month free
- **Netlify/Vercel**: 
  - 100GB bandwidth/month
  - Unlimited sites
- **Railway**: 
  - $5 free credits/month
  - ~500 hours of uptime

### Database:
- Render includes **free PostgreSQL** (1GB storage)
- Railway includes PostgreSQL
- PlanetScale has MySQL free tier (5GB storage)

### To prevent cold starts on Render:
Use a free uptime monitor like:
- https://uptimerobot.com (pings your app every 5 minutes)
- https://cron-job.org

---

## Troubleshooting 🔧

**Backend fails to start:**
- Check environment variables are set
- Verify PostgreSQL connection string
- Check Render logs: Settings → Logs

**Frontend can't connect to backend:**
- Check CORS settings in WebConfig.java
- Verify VITE_API_BASE_URL is correct
- Check browser console for errors

**Database connection fails:**
- Ensure DATABASE_URL is set
- Check PostgreSQL dialect in application.yml
- Verify database credentials

---

## Your Deployment URLs

After deployment, update these:

**Backend**: `https://chatbot-backend-xxxx.onrender.com`
**Frontend**: `https://your-app.netlify.app` or `https://your-app.vercel.app`

**Share your live chatbot with this link!** 🎉

---

Need help? Check the logs:
- **Render**: Dashboard → Your Service → Logs
- **Netlify**: Site Dashboard → Deploys → Deploy log
- **Vercel**: Project → Deployments → Click on deployment
