# AI Live Chat Agent - Phase-wise Implementation Plan

## Project Overview
Build a customer support chat application with AI agent powered by LLM (OpenAI GPT-4o-mini recommended).

**Tech Stack:**
- Backend: Java 17+ with Spring Boot 3.x
- Frontend: React 18+ with Vite
- Database: MySQL 8.0+
- LLM: **Free Option - Groq API (llama-3.1-8b-instant)** or Google Gemini API (free tier)
  - Alternative: OpenAI gpt-4o-mini (very cheap: $0.15/1M tokens, but not free)
- Build Tool: Maven or Gradle
- Optional: Redis for caching

**Estimated Total Time:** 10-14 hours

> **💡 Note on LLM APIs:**
> - **Groq** (Recommended for free): Free tier with generous rate limits (30 req/min). Very fast inference.
> - **Google Gemini**: Free tier available (15 requests/min). Good quality.
> - **Hugging Face**: Free inference API with rate limits.
> - **OpenAI**: Not free, but gpt-4o-mini is very cheap (~$0.03 for 1000 messages).
> - **Ollama** (Local): Completely free, runs locally, but requires GPU and setup.

---

## Phase 1: Project Setup & Infrastructure (1.5-2 hours)

### Tasks:
1. **Initialize Project Structure**
   - [ ] Create backend folder with Spring Boot setup
   - [ ] Create frontend folder with React + Vite
   - [ ] Initialize Git repository with `.gitignore`
   - [ ] Set up monorepo structure (optional) or separate repos

2. **Backend Setup (Spring Boot)**
   - [ ] Initialize Spring Boot project using Spring Initializr (https://start.spring.io/) or IDE
   - [ ] Add core dependencies in `pom.xml` (Maven) or `build.gradle` (Gradle):
     - `spring-boot-starter-web` (REST APIs)
     - `spring-boot-starter-data-jpa` (Database ORM)
     - `mysql-connector-j` (MySQL driver)
     - `lombok` (reduce boilerplate)
     - `spring-boot-starter-validation` (Bean validation)
     - `spring-boot-devtools` (hot reload)
   - [ ] Add HTTP client dependency:
     - `spring-boot-starter-webflux` (WebClient for OpenAI API)
     - OR use OkHttp/RestTemplate
   - [ ] Configure `application.properties` or `application.yml`
   - [ ] Set up folder structure:
     ```
     backend/
     ├── src/
     │   ├── main/
     │   │   ├── java/com/chatbot/
     │   │   │   ├── config/
     │   │   │   ├── controller/
     │   │   │   ├── service/
     │   │   │   ├── repository/
     │   │   │   ├── entity/
     │   │   │   ├── dto/
     │   │   │   ├── exception/
     │   │   │   └── ChatBotApplication.java
     │   │   └── resources/
     │   │       ├── application.yml
     │   │       └── db/migration/ (optional: Flyway)
     │   └── test/
     ├── .env (or use application-local.yml)
     └── pom.xml (or build.gradle)
     ```

3. **Frontend Setup (React + Vite)**
   - [ ] Initialize React project: `npm create vite@latest frontend -- --template react-ts`
   - [ ] Install dependencies:
     - `axios` (HTTP client for API calls)
     - `react-router-dom` (if multi-page needed)
     - `tailwindcss` or CSS framework (optional, for styling)
   - [ ] Set up folder structure:
     ```
     frontend/
     ├── src/
     │   ├── components/
     │   │   ├── Chat/
     │   │   │   ├── ChatContainer.tsx
     │   │   │   ├── MessageList.tsx
     │   │   │   ├── MessageBubble.tsx
     │   │   │   └── ChatInput.tsx
     │   │   └── common/
     │   ├── services/
     │   │   └── chatApi.ts
     │   ├── hooks/
     │   ├── types/
     │   ├── utils/
     │   ├── App.tsx
     │   └── main.tsx
     └── package.json
     ```

4. **Environment Configuration**
   - [ ] Create `application-local.yml` or `application-local.properties` (backend):
     ```yaml
     server:
       port: 8080
     
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/chatbot_db?createDatabaseIfNotExist=true
         username: root
         password: your_password
         driver-class-name: com.mysql.cj.jdbc.Driver
       jpa:
         hibernate:
           ddl-auto: update
         show-sql: true
         properties:
           hibernate:
             dialect: org.hibernate.dialect.MySQL8Dialect
     
     # LLM Configuration (choose one)
     llm:
       provider: groq  # Options: groq, gemini, openai, huggingface
       api:
         key: ${LLM_API_KEY:your_key_here}
         # Groq: https://console.groq.com/keys (FREE)
         # Gemini: https://makersuite.google.com/app/apikey (FREE tier)
         # OpenAI: https://platform.openai.com/api-keys (PAID)
       groq:
         model: llama-3.1-8b-instant
         base-url: https://api.groq.com/openai/v1
       gemini:
         model: gemini-1.5-flash
       openai:
         model: gpt-4o-mini
       max-tokens: 500
       timeout: 15000LLM_API_KEY=your_actual_key`
   - [ ] Get free API key:
     - **Groq** (Recommended): https://console.groq.com/keys - Free, fast, generous limits
     - **Google Gemini**: https://makersuite.google.com/app/apikey - Free tier available
     - **OpenAI**: https://platform.openai.com/api-keys - Requires payment ($5 minimum)
     
     chat:
       max-message-length: 3000
       max-history-messages: 15
     ```
   - [ ] Create `.env` (frontend):
     ```
     VITE_API_BASE_URL=http://localhost:8080/api
     ```
   - [ ] Add `.env` and `application-local.yml` to `.gitignore`
   - [ ] Set environment variable: `export OPENAI_API_KEY=your_actual_key`

### Deliverables:
- ✅ Spring Boot project initialized
- ✅ React + Vite project initialized
- ✅ All dependencies installed
- ✅ Development environment ready
- ✅ Configuration files documented

---

## Phase 2: Database Design & Setup (1.5-2 hours)

### Tasks:
1. **Design Database Schema (MySQL)**
   - [ ] Install MySQL locally or use Docker:
     ```bash
     docker run --name mysql-chatbot -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=chatbot_db -p 3306:3306 -d mysql:8.0
     ```
   - [ ] Design database schema:
     ```sql
     CREATE TABLE conversations (
       id VARCHAR(36) PRIMARY KEY,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       metadata JSON
     );
     
     CREATE TABLE messages (
       id VARCHAR(36) PRIMARY KEY,
       conversation_id VARCHAR(36) NOT NULL,
       sender ENUM('user', 'ai') NOT NULL,
       text TEXT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
       INDEX idx_messages_conversation (conversation_id, created_at)
     );
     ```

2. **JPA Entity Classes**
   - [ ] Create `Conversation` entity with `@Entity` annotation
   - [ ] Create `Message` entity with `@Entity` annotation
   - [ ] Add `MessageSender` enum (USER, AI)
   - [ ] Use UUID generator for IDs
   - [ ] Add `@PrePersist` and `@PreUpdate` lifecycle callbacks
   - [ ] Configure bidirectional relationship (`@OneToMany`, `@ManyToOne`)

3. **Repository Layer (Spring Data JPA)**
   - [ ] Create `ConversationRepository` extending `JpaRepository<Conversation, String>`
   - [ ] Create `MessageRepository` extending `JpaRepository<Message, String>`
   - [ ] Add custom query methods:
     - `findByConversationIdOrderByCreatedAtAsc(String conversationId)`
     - `findRecentMessages(String conversationId, Pageable pageable)`
   - [ ] Test repositories with `@DataJpaTest`

4. **Database Migration (Optional: Flyway or Liquibase)**
   - [ ] Add Flyway dependency
   - [ ] Create migration scripts in `src/main/resources/db/migration/`
   - [ ] Test migrations

### Deliverables:
- ✅ MySQL database running
- ✅ JPA entities created
- ✅ Spring Data repositories implemented
- ✅ Database schema auto-generated or migrated
- ✅ Connection testedLLM API (`ChatCompletionRequest`, `ChatCompletionResponse`, `ChatMessage`)
   - [ ] Implement providers:
     - `GroqProvider` (uses OpenAI-compatible API) - **FREE**
     - `GeminiProvider` (Google's API) - **FREE tier**
     - `OpenAIProvider` (optional, for paid usage)
   - [ ] Use factory pattern to select provider based on config
---

## Phase 3: LLM Integration Service (2-2.5 hours)

### Tasks:
1. **LLM Service Architecture**
   - [ ] Create `src/main/java/com/chatbot/service/llm/` package
   - [ ] Design provider interface:
     ```java
     public interface LLMProvider {
         String generateReply(List<Message> history, String userMessage, LLMConfig config) throws LLMException;
     }Java config class or properties file
   - [ ] Implement context building (last 10-15 messages)
   - [ ] Note: Groq's Llama model works great with this prompt style
   - [ ] Create DTOs for OpenAI API (`OpenAIRequest`, `OpenAIResponse`, `ChatMessage`)
   - [ ] Implement `OpenAIProvider` using `WebClient` or `RestTemplate`
   - [ ] Add `@Service` annotation for Spring dependency injection

2. **Prompt Engineering**
   - [ ] Create system prompt with store knowledge:
     ```
     You are a helpful support agent for "TechGadgets Store", a small e-commerce store.
     
     Store Information:
     - Shipping: Free shipping on orders over $50. Standard shipping takes 3-5 business days. Express shipping available (2 days) for $15.
     - Returns: 30-day return policy. Items must be unused and in original packaging. Full refund within 7 days of return receipt.
     - Support Hours: Monday-Friday 9 AM - 6 PM EST. Email support available 24/7.
     - Payment: We accept all major credit cards, PayPal, and Apple Pay.
     
     Answer clearly and concisely. Be friendly and professional.
     ```
   - [ ] Store FAQ knowledge in config file (`src/config/storeKnowledge.ts`)
   - [ ] Implement context building (last 10-15 messages)

3. **Error Handling & Guardrails**
   - [ ] Add retry logic with exponential backoff
   - [ ] Handle API errors:
     - Invalid API key → friendly error
     - Rate limits → retry after delay
     - Timeout → friendly fallback message
   - [ ] Implement token limits:
     - Max input tokens: ~4000
     - Max output tokens: 500
   - [ ] **Groq Setup Notes:**
     - Use OpenAI-compatible endpoint: `https://api.groq.com/openai/v1/chat/completions`
     - Same request format as OpenAI
     - Much faster responses (~500 tokens/sec)
     - Rate limit: 30 requests/minute (free tier)

### Deliverables:
- ✅ LLM provider interface defined
- ✅ Groq integration working (FREE) or Gemini (FREE tier)e.java`) with `@Service`
   - [ ] Inject configuration properties with `@Value` or `@ConfigurationProperties`
   - [ ] Implement `generateReply()` method
   - [ ] Add conversation history management (limit to last 15 messages)
   - [ ] Add response streaming support (optional, using Server-Sent Events)
   - [ ] Create custom exceptions (`LLMException`, `RateLimitException`)

### Deliverables:
- ✅ LLM provider interface defined
- ✅ OpenAI integration working via HTTP client
- ✅ Prompt with store knowledge
- ✅ Error handling with custom exceptions
- ✅ Token limits enforced

---

## Phase 4: Backend API Implementation (2.5-3 hours)

### Tasks:
1. **Core API Controllers**
   - [ ] Create `ChatController` with `@RestController` and `@RequestMapping("/api/chat")`
   - [ ] Implement `POST /api/chat/message`:
     ```java
     @PostMapping("/message")
     public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request)
     // Request: { message: String, sessionId: String (optional) }
     // Response: { reply: String, sessionId: String, messageId: String }
     ```
   - [ ] Implement `GET /api/chat/{sessionId}/history`:
     ```java
     @GetMapping("/{sessionId}/history")
     public ResponseEntity<ChatHistoryResponse> getHistory(@PathVariable String sessionId)
     // Response: { messages: List<MessageDTO>, sessionId: String }
     ```
   - [ ] Implement `POST /api/chat/new`:
     ```java
     @PostMapping("/new")
     public ResponseEntity<SessionResponse> createSession()
     // Response: { sessionId: String }
     ```

2. **Request Validation (Bean Validation)**
   - [ ] Create DTO classes with validation annotations:
     - `ChatRequest` with `@NotBlank`, `@Size(max=3000)` on message
     - `@Pattern` for sessionId (UUID format)
   - [ ] Add `@Valid` annotation on controller parameters
   - [ ] Configure validation error messages
   - [ ] Return clear error responses (400 Bad Request)

3. **Global Exception Handler**
   - [ ] Create `@ControllerAdvice` class (`GlobalExceptionHandler`)
   - [ ] Handle different exceptions with `@ExceptionHandler`:
     - `MethodArgumentNotValidException` → 400 (validation errors)
     - `EntityNotFoundException` → 404
     - `LLMException` → 503 (Service Unavailable)
     - `Exception` → 500 (Internal errors)
   - [ ] Create `ErrorResponse` DTO
   - [ ] Log errors with SLF4J
   - [ ] Never expose internal details to client

4. **Chat Service Layer**
   - [ ] Create `ChatService` class with `@Service` annotation
   - [ ] Inject repositories and LLMService via constructor
   - [ ] Implement business logic methods:
     - `createNewConversation()`
     - `sendMessage(String sessionId, String message)`
     - `getConversationHistory(String sessionId, Integer limit)`
   - [ ] Add `@Transactional` annotation for atomic operations
   - [ ] Orchestrate between repositories and LLM service:
     1. Validate session exists (or create new)
     2. Save user message to DB
     3. Fetch conversation history
     4. Call LLM service
     5. Save AI response to DB
     6. Return response

5. **CORS & Security Configuration**
   - [ ] Create `WebConfig` class with `@Configuration`
   - [ ] Configure CORS with `WebMvcConfigurer` for frontend origin
   - [ ] Add rate limiting (optional): Bucket4j or Spring Cloud Gateway
   - [ ] Configure request logging with `CommonsRequestLoggingFilter`
   - [ ] Add security headers configuration

### Deliverables:
- ✅ REST API endpoints working
- ✅ Input validation with Bean Validation
- ✅ Error handling with @ControllerAdvice
- ✅ Business logic separated in @Service layer
- ✅ API testable with Postman/curl
- ✅ CORS configured

---

## Phase 5: Frontend Chat UI (2.5-3 hours)

### Tasks:
1. **Chat UI Components (React)**
   - [ ] Create `ChatContainer.tsx` (main component)
   - [ ] Create `MessageList.tsx`:
     - Scrollable container with `useRef` for auto-scroll
     - Auto-scroll to bottom on new message with `useEffect`
     - Distinguish user vs AI messages (different styling)
   - [ ] Create `MessageBubble.tsx`:
     - Props: `sender`, `text`, `timestamp`
     - Different styles for user/AI
     - TypeScript interface for message type
   - [ ] Create `ChatInput.tsx`:
     - Text input + send button
     - Enter key to send (onKeyPress handler)
     - Disable while sending
     - Character counter (optional)

2. **State Management (React Hooks)**
   - [ ] Use `useState` hooks in `ChatContainer.tsx`:
     - `messages` array
     - `sessionId`
     - `isLoading` flag
     - `error` message
   - [ ] Create custom hooks (optional):
     - `useChat()` hook for chat logic
     - `useLocalStorage()` for session persistence
   - [ ] Implement handler functions:
     - `loadHistory(sessionId)`
     - `handleSendMessage(text)`
     - `createNewSession()`

3. **API Client (Axios)**
   - [ ] Create API client (`src/services/chatApi.ts`)
   - [ ] Configure axios instance with base URL from env
   - [ ] Implement methods:
     - `sendMessage(sessionId, message)`
     - `getHistory(sessionId)`
     - `createNewSession()`
   - [ ] Add axios interceptors for error handling
   - [ ] Add timeout configuration (15 seconds)

4. **UX Features**
   - [ ] Add "Agent is typing..." indicator component
   - [ ] Add loading states (disabled input, spinner)
   - [ ] Add error message display (toast/banner component)
   - [ ] Empty state when no messages
   - [ ] Timestamp display for messages (format with date-fns)
   - [ ] Smooth scroll behavior with CSS

5. **Session Management**
   - [ ] Store sessionId in localStorage
   - [ ] Load existing session on mount with `useEffect`
   - [ ] "New conversation" button component
   - [ ] Handle session expiry gracefully
   - [ ] Clear localStorage on new session

### Deliverables:
- ✅ Functional React chat UI
- ✅ Messages display correctly
- ✅ Input handling with controlled components
- ✅ State management with hooks
- ✅ UX features implemented
- ✅ TypeScript types defined

---

## Phase 6: Testing & Robustness (1.5-2 hours)

### Tasks:
1. **Backend Testing**
   - [ ] Test empty message → should reject
   - [ ] Test very long message (>3000 chars) → should reject/truncate
   - [ ] Test invalid sessionId → should create new or error gracefully
   - [ ] Test LLM API failure → should return friendly error
   - [ ] Test database connection failure → should handle gracefully
   - [ ] Test concurrent requests → should not crash

2. **Frontend Testing**
   - [ ] Test sending messages
   - [ ] Test loading conversation history
   - [ ] Test network errors (disconnect, timeout)
   - [ ] Test rapid message sending
   - [ ] Test very long message input
   - [ ] Test mobile responsiveness (optional but nice)

3. **Error Scenarios**
   - [ ] Invalid/missing API key
   - [ ] Rate limit exceeded
   - [ ] Network timeout
   - [ ] Database down
   - [ ] Malformed input (XSS attempts, SQL injection)

4. **Edge Cases**
   - [ ] Empty conversation
   - [ ] First message in conversation
   - [ ] Very long conversation history
   - [ ] Special characters in messages
   - [ ] Emoji support

### Deliverables:
- ✅ All error cases handled
- ✅ Input validation working
- ✅ No crashes on bad input
- ✅ Friendly error messages
- ✅ Edge cases covered

---

## Phase 7: Polish & Documentation (1-1.5 hours)

### Tasks:
1. **Code Quality**
   - [ ] Add comments to complex logic
   - [ ] Run ESLint/Prettier
   - [ ] Remove console.logs (replace with proper logging)
   - [ ] Check for TypeScript errors
   - [ ]Get FREE API key from Groq (https://console.groq.com/keys) or Gemini
     5. Set LLM_API_KEY environment variable
     6. Build backend: `mvn clean install` or `./gradlew build`
     7. Run backend: `mvn spring-boot:run` or `java -jar target/chatbot.jar`
     8. Install frontend dependencies: `npm install`
     9 ] Tech stack used
   - [ ] Prerequisites (Java 17+, Maven/Gradle, MySQL 8.0+, Node.js 18+)
   - [ ] Local setup instructions:
     1. Clone repo
     2. Install MySQL and create database
     3. Configure application.yml
     4. Set OPENAI_API_KEY environment variable
     5. Build backend: `mvn clean install` or `./gradlew build`
     6. Run backend: `mvn spring-boot:run` or Groq/Gemini)
     - Database schema (JPA entities)
   - [ ] LLM notes:
     - Provider used (Groq or Gemini - FREE)
     - Model used (llama-3.1-8b-instant or gemini-1.5-flash)
     - Why Groq: Free, fast (500+ tokens/sec), generous rate limitsation
   - [ ] Architecture overview:
     - Backend layers (Controller → Service → Repository)
     - LLM integration approach (WebClient to OpenAI)
     - Database schema (JPA entities)
   - [ ] LLM notes:
     - Provider used (OpenAI)
     - Model used (gpt-4o-mini)
     - Prompt strategy
     - Token limits
   - [ ] Trade-offs & future improvements:
     - What was prioritized
     - What was left out due to time
     - What would you add next

3. **Environment Setup**
   - [ ] Create detailed `.env.example`
   - [ ] Add setup scripts (optional):
     - `npm run setup` (run migrations)
     - `npm run seed` (seed initial data)
   - [ ] Test clean setup on fresh machine

4. **UI Polish**
   - [ ] Basic styling (make it look decent)
   - [ ] Loading states polished
   - [ ] Error messages styled
   - [ ] Mobile-friendly (basic responsiveness)

### Deliverables:
- ✅ Clean, readable code
- ✅ Comprehensive README
- ✅ Easy to set up locally
- ✅ Professional appearance

---

## Phase 8: Deployment (1.5-2 hours)
LLM_API_KEY` (Groq or Gemini API key - FREE)
     - `LLM_PROVIDER` (set to `groq` or `gemini`)
### Tasks:
1. **Backend Deployment (Render.com or Railway recommended)**
   - [ ] Create `Dockerfile` for Spring Boot (optional, or use JAR)
   - [ ] Configure build command: `mvn clean package -DskipTests` or `./gradlew build`
   - [ ] Configure start command: `java -jar target/chatbot-0.0.1-SNAPSHOT.jar`
   - [ ] Set environment variables in Render:
     - `SPRING_DATASOURCE_URL`
     - `SPRING_DATASOURCE_USERNAME`
     - `SPRING_DATASOURCE_PASSWORD`
     - `OPENAI_API_KEY`
   - [ ] Set up MySQL database on Render or use external (e.g., PlanetScale)
   - [ ] Set `spring.jpa.hibernate.ddl-auto=update` for auto-migration
   - [ ] Test deployed API endpoints

2. **Frontend Deployment (Vercel or Netlify recommended)**
   - [ ] Configure build command: `npm run build`
   - [ ] Configure output directory: `dist`
   - [ ] Set environment variable: `VITE_API_BASE_URL` to production backend URL
   - [ ] Deploy to Vercel or Netlify
   - [ ] Test deployed frontend

3. **Database Setup**
   - [ ] Use Render MySQL or PlanetScale (free tier)
   - [ ] Configure connection string in application.yml
   - [ ] Set `spring.jpa.hibernate.ddl-auto=update` for auto-schema creation
   - [ ] Verify connections and test with sample data

4. **Final Testing**
   - [ ] Test end-to-end on deployed URLs
   - [ ] Test from mobile device
   - [ ] Test error cases on production
   - [ ] Verify no hardcoded secrets exposed

5. **Documentation Update**
   - [ ] Add deployment URLs to README
   - [ ] Document deployment process
   - [ ] Add troubleshooting section

### Deliverables:
- ✅ Backend deployed and accessible
- ✅ Frontend deployed and accessible
- ✅ Database hosted and connected
- ✅ End-to-end working in production
- ✅ URLs in README

---

## Dependency Flow

```
Phase 1 (Setup) 
    ↓
Phase 2 (Database) ← Phase 3 (LLM)
    ↓                      ↓
Phase 4 (Backend API) ←----┘
    ↓
Phase 5 (Frontend)
    ↓
Phase 6 (Testing)
    ↓
Phase 7 (Documentation & Polish)
    ↓
Phase 8 (Deployment)
```

---

## Time Allocation Summary

| Phase | Estimated Time | Priority |
|-------|---------------|----------|
| 1. Setup | 1.5-2h | Critical |
| 2. Database | 1.5-2h | Critical |
| 3. LLM Integration | 2-2.5h | Critical |
| 4. Backend API | 2.5-3h | Critical |
| 5. Frontend UI | 2.5-3h | Critical |
| 6. Testing & Robustness | 1.5-2h | High |
| 7. Documentation & Polish | 1-1.5h | High |
| 8. Deployment | 1.5-2h | High |
| **Total** | **14-18h** | - |

---

## Critical Success Factors

### Must Have (MVP):
- ✅ End-to-end chat working (user types → AI responds)
- ✅ Messages persisted in database
- ✅ Session/conversation management
- ✅ LLM integration with proper error handling
- ✅ Input validation (empty, too long)
- ✅ FAQ knowledge in AI responses
- ✅ Clean, working UI
- ✅ Deployed and accessible
- ✅ Good README

### Nice to Have (If time permits):
- ⭐ Response streaming
- ⭐ "Agent is typing" indicator
- ⭐ Message timestamps
- ⭐ Redis caching
- ⭐ Rate limiting
- ⭐ Conversation history in sidebar
- ⭐ Export conversation
- ⭐ Better mobile UX

### Can Skip:
- ❌ User authentication
- ❌ Multiple agent personas
- ❌ Rich message formatting (images, files)
- ❌ Real-time typing indicators (WebSocket)
- ❌ Analytics dashboard
- ❌ Admin panel

--- (FREE Options)**
- **Recommended:** Groq API with `llama-3.1-8b-instant`
- **Reason:** 
  - ✅ **Completely FREE** with generous rate limits (30 req/min)
  - ✅ Extremely fast (~500 tokens/sec)
  - ✅ OpenAI-compatible API (easy integration)
  - ✅ Good quality for customer support use case
- **Alternative Free Options:**
  - Google Gemini 1.5 Flash (free tier: 15 req/min)
  - Hugging Face Inference API (free with limits)
- **Paid Alternative:** OpenAI gpt-4o-mini ($0.15/1M tokens - very cheap but not free
### 1. **Database Choice**
- **Recommended:** MySQL 8.0+ (via Render/PlanetScale free tier)
- **Alternative:** H2 (in-memory, for quick local development only)
- **Reason:** MySQL is production-ready, free tier available, widely supported, easy integration with Spring Boot

### 2. **LLM Provider**
- **Recommended:** OpenAI `gpt-4o-mini`
- **Reason:** Fast, cheap ($0.15/1M input tokens), good quality, easy SDK
- **Alternative:** Anthropic Claude 3.5 Haiku (similar price, good guardrails)

### 3. **Frontend Framework**
- **Recommended:** React 18+ with Vite
- **Reason:** Industry standard, large ecosystem, excellent TypeScript support, fast with Vite
- **Alternative:** Vue or Angular (if more comfortable)

### 4. **Deployment Platform**
- **Backend:** Render.com or Railway.app (free tier, Java support, MySQL integration)
- **Frontend:** Vercel or Netlify (free, automatic deployments, optimized for React/Vite)
- **Database:** PlanetScale (MySQL, free tier) or Render MySQL
- **Alternative:** Fly.io, AWS Free Tier, or Azure

### 5. **Architecture Pattern**
```
Frontend (React + Vite)
    ↓ HTTP/REST (Axios)
Backend (Spring Boot)
    ├── Controller Layer (@RestController)
    │   └── ChatController (API endpoints)
    ├── Service Layer (@Service)
    │   ├── ChatService (business logic)
    │   └── LLMSer✅ **Use Groq (FREE)** or Gemini free tier, cap max tokens, implement request limits |
| API rate limits | Groq: 30 req/min free tier. Add retry with backoff, handle 429 errors gracefully |
| Database connection issues | Add connection pooling, graceful error handling |
| Long message handling | Validate max length (3000 chars), truncate if needed |
| Deployment complexity | Use platforms with free tiers and good docs (Render, Vercel) |
| Time overrun | Focus on MVP first, skip nice-to-haves if needed |

---

## 🆓 How to Get FREE LLM API Access

### Option 1: Groq (RECOMMENDED)
1. Visit https://console.groq.com/keys
2. Sign up (email or Google)
3. Generate API key
4. **Free Tier:** 30 requests/minute, 14,400 requests/day
5. **Models:** llama-3.1-8b-instant (very fast), mixtral-8x7b

### Option 2: Google Gemini
1. Visit https://makersuite.google.com/app/apikey
2. Sign in with Google account
3. Create API key
4. **Free Tier:** 15 requests/minute, 1,500 requests/day
5. **Model:** gemini-1.5-flash

### Option 3: Hugging Face
1. Visit https://huggingface.co/settings/tokens
2. Create account and generate token
3. Use Inference API (free tier available)
4. Rate limits vary by model
        └── messages table
```

---

## Risk Mitigation

| Risk | Mitigation |
|------|------------|
| LLM API costs | Use `gpt-4o-mini`, cap max tokens, implement request limits |
| API rate limits | Add retry with backoff, handle 429 errors gracefully |
| Database connection issues | Add connection pooling, graceful error handling |
| Long message handling | Validate max length (3000 chars), truncate if needed |
| Deployment complexity | Use platforms with free tiers and good docs (Render, Vercel) |
| Time overrun | Focus on MVP first, skip nice-to-haves if needed |

---

## Next Steps

**Choose your approach:**

1. **Full Implementation:** "Proceed with Phase 1 - let's build everything"
2. **Selective Implementation:** "Start with Phase X" (specify which phase)
3. **Review & Adjust:** "Let me review this plan first, I have questions"
4. **Generate Code Only:** "Just show me the code structure for Phase X"

Let me know how you'd like to proceed! 🚀
