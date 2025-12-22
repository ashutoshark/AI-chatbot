# ✅ DEPLOYMENT CHECKLIST

Use this checklist to deploy your AI chatbot to production.

## 📋 Pre-Deployment Checklist

- [ ] Backend builds successfully: `mvn clean package`
- [ ] Frontend builds successfully: `npm run build`
- [ ] You have Groq API key from https://console.groq.com/keys
- [ ] PostgreSQL dependency added to pom.xml ✅ (already done)
- [ ] Production config file exists ✅ (already done)

## 🎯 Deployment Steps

### Backend (Render.com)

- [ ] 1. Create Render account: https://render.com/register
- [ ] 2. Create PostgreSQL database
  - [ ] Name: `chatbot-db`
  - [ ] Copy External Database URL
- [ ] 3. Create Web Service
  - [ ] Runtime: Java
  - [ ] Build command: `cd backend && mvn clean package -DskipTests`
  - [ ] Start command: `java -Dserver.port=$PORT -jar backend/target/backend-1.0.0.jar`
- [ ] 4. Set environment variables:
  - [ ] `LLM_API_KEY` = your Groq API key
  - [ ] `DATABASE_URL` = PostgreSQL URL from step 2
  - [ ] `DB_DRIVER` = org.postgresql.Driver
  - [ ] `HIBERNATE_DIALECT` = org.hibernate.dialect.PostgreSQLDialect
  - [ ] `SPRING_PROFILES_ACTIVE` = production
- [ ] 5. Deploy and wait for "Live" status
- [ ] 6. Copy backend URL: `https://_____________________.onrender.com`

### Frontend (Netlify)

- [ ] 7. Create Netlify account: https://app.netlify.com/signup
- [ ] 8. Build frontend locally:
  ```bash
  cd frontend
  echo VITE_API_BASE_URL=https://YOUR-BACKEND-URL.onrender.com/api > .env.production
  npm run build
  ```
- [ ] 9. Deploy to Netlify
  - [ ] Drag `frontend/dist` folder to Netlify
  - [ ] Wait for deployment
- [ ] 10. Copy frontend URL: `https://_____________________.netlify.app`

### Final Steps

- [ ] 11. Update CORS in `backend/src/main/java/com/chatbot/config/WebConfig.java`
  - [ ] Add your Netlify URL to `allowedOrigins`
  - [ ] Push to GitHub
  - [ ] Redeploy on Render
- [ ] 12. Test live application
  - [ ] Visit Netlify URL
  - [ ] Send test message
  - [ ] Verify AI response works
- [ ] 13. Share your chatbot! 🎉

## 🌐 Your Deployed URLs

**Backend**: `https://_____________________.onrender.com`

**Frontend**: `https://_____________________.netlify.app`

**Database**: Render PostgreSQL

**API Key**: Groq (free tier)

---

## ⚠️ Important Notes

1. **First Load**: Takes 30-60 seconds (cold start). This is normal for free tier.

2. **Keep It Alive**: Use https://uptimerobot.com to ping your backend every 5 minutes to prevent sleep.

3. **Free Limits**:
   - Render: 750 hours/month
   - Netlify: 100GB bandwidth/month
   - PostgreSQL: 1GB storage
   - Groq: Free tier limits apply

4. **Auto-Deploy**: Both platforms support GitHub integration for automatic deploys on push.

---

## 🆘 Troubleshooting

### Backend not deploying?
- Check Render logs
- Verify all environment variables are set
- Ensure PostgreSQL database is "Available"

### Frontend can't connect?
- Check backend URL in `.env.production`
- Verify CORS settings include your Netlify domain
- Check browser console (F12) for errors

### Database errors?
- Verify DATABASE_URL is correct
- Check HIBERNATE_DIALECT is set to PostgreSQL
- Ensure database credentials are valid

---

## 📚 Documentation

- **Step-by-step guide**: [DEPLOY_INSTRUCTIONS.md](./DEPLOY_INSTRUCTIONS.md)
- **All options**: [DEPLOY_NOW.md](./DEPLOY_NOW.md)
- **Advanced**: [DEPLOYMENT.md](./DEPLOYMENT.md)

---

**Ready to deploy? Start with Step 1!** 🚀
