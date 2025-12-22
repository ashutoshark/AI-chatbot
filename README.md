# 🤖 AI Chatbot - Full Stack Application

**A production-ready AI chatbot built with Spring Boot, React, and Groq AI.**

![Tech Stack](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green) ![React](https://img.shields.io/badge/React-19-blue) ![TypeScript](https://img.shields.io/badge/TypeScript-5.9-blue) ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

## ✨ Features

- 💬 Real-time AI conversations powered by Groq API (free tier)
- 🎨 Modern, responsive UI with smooth animations
- 💾 Persistent conversation history with MySQL
- 🔄 Support for multiple conversations
- ⚡ Fast responses with llama-3.1-8b-instant model
- 🚀 Deployment-ready build configuration
- 🔒 Input validation and error handling
- 📱 Mobile-friendly design

## 🏗️ Architecture

```
┌─────────────┐      REST API      ┌─────────────┐      HTTP      ┌─────────────┐
│   React     │ ←──────────────→   │ Spring Boot │ ←───────────→  │  Groq API   │
│  Frontend   │   (Port 3000)      │   Backend   │                │  (LLM)      │
│             │                     │             │                └─────────────┘
└─────────────┘                     └─────────────┘
                                           ↓
                                     JPA/Hibernate
                                           ↓
                                    ┌─────────────┐
                                    │   MySQL     │
                                    │  Database   │
                                    └─────────────┘
```

## 📁 Project Structure

```
AI-chatBot/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/chatbot/
│   │   ├── config/            # CORS, WebClient config
│   │   ├── controller/        # REST endpoints
│   │   ├── dto/               # Request/Response objects
│   │   ├── entity/            # JPA entities
│   │   ├── exception/         # Error handling
│   │   ├── repository/        # Database access
│   │   ├── service/           # Business logic
│   │   └── ChatBotApplication.java
│   ├── src/main/resources/
│   │   └── application.yml    # Configuration
│   ├── pom.xml                # Maven dependencies
│   └── target/                # Build output
│
├── frontend/                  # React frontend
│   ├── src/
│   │   ├── components/       # UI components
│   │   │   ├── ChatContainer.tsx
│   │   │   ├── MessageList.tsx
│   │   │   ├── MessageBubble.tsx
│   │   │   └── ChatInput.tsx
│   │   ├── services/         # API integration
│   │   │   └── chatApi.ts
│   │   ├── types/            # TypeScript types
│   │   │   └── chat.ts
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── package.json
│   ├── vite.config.ts
│   └── dist/                 # Production build
│
├── docs/                      # Documentation
│   ├── LEARNING_GUIDE.md
│   └── COMMON_MISTAKES.md
├── DEPLOYMENT.md             # Deployment guide
├── IMPLEMENTATION_PLAN.md    # Development roadmap
└── README.md                 # This file
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+** - [Download](https://adoptium.net/)
- **Maven 3.6+** - Usually bundled with Java
- **Node.js 20+** - [Download](https://nodejs.org/)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/)
- **Groq API Key** - [Get Free Key](https://console.groq.com/keys)

### Local Development

### 1. Setup Database

```sql
CREATE DATABASE chatbot_db;
```

### 2. Configure Backend

Edit `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    username: root          # Your MySQL username
    password: your_password # Your MySQL password

llm:
  api:
    key: your_groq_api_key  # Get from https://console.groq.com/keys
```

### 3. Start Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on **http://localhost:8081**

### 4. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on **http://localhost:3000**

### 5. Open Browser

Visit **http://localhost:3000** and start chatting! 🎉

---

## 🌐 Deploy to Production (FREE!)

Deploy your chatbot for **FREE** in 15 minutes:

### Quick Deploy Options

| Platform | Purpose | Free Tier | Deploy Time |
|----------|---------|-----------|-------------|
| **[Render](https://render.com)** | Backend + Database | 750 hrs/month | 5 min |
| **[Netlify](https://netlify.com)** | Frontend | 100GB bandwidth | 3 min |
| **[Vercel](https://vercel.com)** | Frontend | 100GB bandwidth | 3 min |
| **[Railway](https://railway.app)** | Backend + DB | $5 credit/month | 5 min |

### 🎯 Recommended: Render + Netlify

**Total Cost: $0/month** • **Total Time: 15 minutes**

1. **Backend on Render** (with free PostgreSQL)
   - Sign up at https://render.com
   - Create PostgreSQL database
   - Deploy backend web service
   - Copy backend URL

2. **Frontend on Netlify** (drag & drop)
   - Sign up at https://netlify.com
   - Build: `cd frontend && npm run build`
   - Drag `frontend/dist` folder to Netlify
   - Add backend URL as environment variable
   - Done! ✨

### 📖 Detailed Instructions

See **[DEPLOY_INSTRUCTIONS.md](./DEPLOY_INSTRUCTIONS.md)** for complete step-by-step guide.

**Alternative guides:**
- [DEPLOY_NOW.md](./DEPLOY_NOW.md) - All deployment options
- [DEPLOYMENT.md](./DEPLOYMENT.md) - Advanced deployment strategies

---

## 📦 Build for Production

Run the build script:

### 5. Open Browser

Visit **http://localhost:3000** and start chatting! 🎉

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/chat` | Send message and get AI response |
| POST | `/api/conversations` | Create new conversation |
| GET | `/api/conversations/{id}` | Get conversation details |
| GET | `/api/conversations/{id}/messages` | Get all messages |
| DELETE | `/api/conversations/{id}` | Delete conversation |

### Example API Request

```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message":"Hello, how are you?"}'
```

Response:
```json
{
  "conversationId": "9b172278-0430-46d5-9357-613bd44a15c7",
  "messageId": "3fbe14bb-d299-45fd-91a8-9f029faed3c7",
  "message": "I'm doing great! How can I help you today?",
  "sender": "ai",
  "timestamp": "2025-12-22T21:38:16"
}
```

## 🛠️ Tech Stack Details

### Backend
- **Spring Boot 3.2.1** - Java framework
- **Spring Data JPA** - Database ORM
- **Hibernate** - JPA implementation
- **MySQL Connector** - Database driver
- **WebClient** - HTTP client for AI API
- **Bean Validation** - Input validation

### Frontend
- **React 19** - UI library
- **TypeScript 5.9** - Type safety
- **Vite 6** - Build tool
- **Axios** - HTTP client
- **CSS3** - Styling (no frameworks)

### AI & Database
- **Groq API** - LLM provider (free tier)
- **llama-3.1-8b-instant** - AI model
- **MySQL 8.0** - Relational database

## 📦 Build for Production

### Backend
```bash
cd backend
mvn clean package
# Output: backend/target/backend-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
# Output: frontend/dist/
```

See [DEPLOYMENT.md](./DEPLOYMENT.md) for detailed deployment instructions.

## 🧪 Testing the Application

1. Start both backend and frontend
2. Open http://localhost:3000
3. Type a message: "Tell me a joke"
4. AI responds within 1-2 seconds
5. Click "New Chat" to start fresh conversation
6. Messages persist in database

## 🔧 Configuration Options

### Change AI Model

Edit `application.yml`:
```yaml
llm:
  groq:
    model: llama-3.1-70b-versatile  # More powerful model
```

### Change Server Port

```yaml
server:
  port: 8080  # Change from 8081
```

Update frontend `.env`:
```
VITE_API_BASE_URL=http://localhost:8080/api
```

### Enable SQL Logging

```yaml
spring:
  jpa:
    show-sql: true  # See all SQL queries
```

## 🐛 Troubleshooting

### Backend Issues

**Error: Cannot connect to database**
```bash
# Check MySQL is running
mysql -u root -p

# Check credentials in application.yml
```

**Error: Port 8081 already in use**
```bash
# Find process using port
netstat -ano | findstr :8081

# Kill process (Windows)
taskkill /PID <process_id> /F
```

### Frontend Issues

**Error: Failed to fetch**
- Ensure backend is running on port 8081
- Check browser console for CORS errors
- Verify VITE_API_BASE_URL in `.env`

**Build fails**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

## 📚 Learning Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Groq API Docs](https://console.groq.com/docs)

## 🤝 Contributing

Contributions welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push and create a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 🙏 Acknowledgments

- **Groq** for providing free AI API
- **Spring Boot** team for excellent documentation
- **React** community for great tools

---

**Built with ❤️ using Spring Boot, React, and Groq AI**

For detailed deployment instructions, see [DEPLOYMENT.md](./DEPLOYMENT.md)

For implementation details, see [IMPLEMENTATION_PLAN.md](./IMPLEMENTATION_PLAN.md)

### Step 1: Get a FREE AI API Key

We use **Groq** (it's FREE and fast!):

1. Go to: https://console.groq.com/keys
2. Sign up with your email
3. Click "Create API Key"
4. Copy the key (looks like: `gsk_...`)
5. Save it somewhere safe!

### Step 2: Setup Database

```bash
# Start MySQL (if using Docker)
docker run --name chatbot-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=chatbot_db -p 3306:3306 -d mysql:8.0

# OR install MySQL normally and create database
mysql -u root -p
CREATE DATABASE chatbot_db;
```

### Step 3: Run Backend (Server)

```bash
cd backend

# Copy environment file
cp application-example.yml src/main/resources/application-local.yml

# Edit application-local.yml and add your Groq API key
# Change: LLM_API_KEY=your_groq_key_here

# Build the project (downloads all dependencies)
mvn clean install

# Run the server (starts on http://localhost:8080)
mvn spring-boot:run
```

### Step 4: Run Frontend (UI)

```bash
cd frontend

# Install dependencies (like npm install)
npm install

# Start development server (starts on http://localhost:5173)
npm run dev
```

### Step 5: Test It!

1. Open browser: http://localhost:5173
2. Type a message: "What's your return policy?"
3. Watch the AI respond! 🎉

## 🐛 Common Issues & Fixes

### Issue 1: "Port 8080 already in use"
**Solution:** Something else is using port 8080
```bash
# Windows: Find and kill process
netstat -ano | findstr :8080
taskkill /PID <process_id> /F

# Or change port in application.yml:
server:
  port: 8081
```

### Issue 2: "Cannot connect to MySQL"
**Solution:** Check MySQL is running
```bash
# Check if MySQL is running
docker ps  # if using Docker
# OR
mysql -u root -p  # try to connect
```

### Issue 3: "Groq API key invalid"
**Solution:** Check your API key
- Make sure you copied the whole key (starts with `gsk_`)
- Check no extra spaces in application-local.yml
- Generate a new key from https://console.groq.com/keys

## 📖 Learning Resources

1. **[LEARNING_GUIDE.md](docs/LEARNING_GUIDE.md)** - Start here! Explains every concept
2. **[CODE_WALKTHROUGH.md](docs/CODE_WALKTHROUGH.md)** - Line-by-line code explanations
3. **[COMMON_MISTAKES.md](docs/COMMON_MISTAKES.md)** - Learn from our mistakes
4. **[API_EXAMPLES.md](docs/API_EXAMPLES.md)** - How to test with Postman/curl

## 🎓 For Students

This project is designed to be:
- ✅ **Over-commented** - Every line explained
- ✅ **Beginner-friendly** - No assumed knowledge
- ✅ **Real-world** - Actually works and deployable
- ✅ **Free to use** - No API costs (Groq is free!)

### How to Use This for Learning

1. **Read the code** - Lots of comments explain WHY, not just WHAT
2. **Check docs/** - Deep dives into concepts
3. **Make mistakes** - We included common mistakes and fixes!
4. **Experiment** - Change things and see what happens
5. **Ask questions** - Add your own comments and notes

## 🚀 Next Steps

Once you understand the basics:
1. Add new features (suggested in docs)
2. Deploy to production (guide included)
3. Add authentication (bonus feature)
4. Customize the UI
5. Add more AI capabilities

## 📝 Project Notes

**What makes this different:**
- Comments everywhere (maybe too many!)
- Includes "wrong" code with corrections
- Explains WHY we do things, not just HOW
- Real student project feel
- Learning-first approach

**Tech Stack:**
- Backend: Java + Spring Boot (industry standard)
- Frontend: React + TypeScript (most popular)
- Database: MySQL (widely used)
- AI: Groq API (FREE!)

## 🤝 Contributing

Found a better way to explain something? Add it!
Made a mistake? Document it in COMMON_MISTAKES.md!
Learned something new? Share in LEARNING_GUIDE.md!

## 📞 Help & Support

Stuck? Check:
1. COMMON_MISTAKES.md first
2. Each file has lots of comments
3. docs/ folder has detailed guides
4. Google the error message (we all do this!)

---

Made with ❤️ for learning | No prior experience required | Start small, learn big!
