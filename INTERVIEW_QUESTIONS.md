# 📝 AI Chatbot - Interview Questions & Answers

This document contains common interview questions that you might be asked about this project. Use these to prepare for your interviews!

---

## 🎯 Project Overview Questions

### Q1: Can you explain what this project does?
**Answer:** 
This is an AI-powered chatbot application. Users can send messages through a web interface, and the application sends these messages to an AI service (Groq API) which generates intelligent responses. The conversations are stored in a database so users can have contextual conversations where the AI remembers previous messages.

### Q2: What technologies did you use in this project?
**Answer:**
- **Backend:** Java 17, Spring Boot 3.2, Spring Data JPA, Hibernate
- **Frontend:** React 18, TypeScript, Vite
- **Database:** PostgreSQL (production), MySQL (development)
- **AI Service:** Groq API with LLaMA 3.1 model
- **Deployment:** Render.com (free tier)
- **Others:** Maven (build tool), Axios (HTTP client)

### Q3: Why did you choose these technologies?
**Answer:**
- **Spring Boot:** Industry standard for Java web applications, auto-configuration saves time
- **React:** Popular, component-based, great for building interactive UIs
- **TypeScript:** Adds type safety to JavaScript, catches errors early
- **PostgreSQL:** Robust, free, and widely used in production environments
- **Groq API:** Fast inference, free tier available, OpenAI-compatible API

---

## 🏗️ Architecture Questions

### Q4: Can you explain the architecture of this application?
**Answer:**
The application follows a 3-tier architecture:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    Frontend     │────▶│    Backend      │────▶│    Database     │
│   (React/TS)    │◀────│ (Spring Boot)   │◀────│  (PostgreSQL)   │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
                                 │
                                 ▼
                        ┌─────────────────┐
                        │    Groq AI      │
                        │    Service      │
                        └─────────────────┘
```

1. **Frontend (Presentation Layer):** Handles user interface and user interactions
2. **Backend (Business Logic Layer):** Processes requests, manages conversations, calls AI API
3. **Database (Data Layer):** Stores conversations and messages

### Q5: What design patterns did you use?
**Answer:**
1. **MVC Pattern:** Separates Model (Entity), View (React), Controller (REST Controller)
2. **Repository Pattern:** Data access is abstracted through repository interfaces
3. **DTO Pattern:** Data Transfer Objects separate internal entities from API responses
4. **Service Layer Pattern:** Business logic is encapsulated in service classes
5. **Dependency Injection:** Spring automatically injects dependencies

---

## 💾 Database Questions

### Q6: What is the database schema for this project?
**Answer:**
Two tables with a one-to-many relationship:

```sql
-- Conversations table
CREATE TABLE conversations (
    id VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Messages table
CREATE TABLE messages (
    id VARCHAR(36) PRIMARY KEY,
    conversation_id VARCHAR(36) REFERENCES conversations(id),
    sender VARCHAR(10),  -- 'user' or 'ai'
    text TEXT,
    created_at TIMESTAMP
);
```

**Relationship:** One Conversation has Many Messages (1:N)

### Q7: What is JPA and why did you use it?
**Answer:**
JPA (Java Persistence API) is a specification for managing relational data in Java applications. I used it because:

1. **Object-Relational Mapping (ORM):** Maps Java objects to database tables automatically
2. **No SQL Writing:** Most database operations don't require writing SQL
3. **Database Independence:** Can switch databases without changing code
4. **Productivity:** Reduces boilerplate code significantly

### Q8: What is the difference between `@Entity` and `@Table` annotations?
**Answer:**
- `@Entity`: Marks a class as a JPA entity (database object)
- `@Table`: Specifies the table name in the database

```java
@Entity                    // This class is a database entity
@Table(name = "messages")  // Maps to 'messages' table
public class Message { }
```

---

## 🌐 API Questions

### Q9: What REST endpoints does your application have?
**Answer:**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/chat` | Send message, get AI response |
| POST | `/api/conversations` | Create new conversation |
| GET | `/api/conversations/{id}` | Get conversation by ID |
| GET | `/api/conversations/{id}/messages` | Get all messages |
| DELETE | `/api/conversations/{id}` | Delete conversation |
| GET | `/api/health` | Health check endpoint |

### Q10: What is REST API?
**Answer:**
REST (Representational State Transfer) is an architectural style for designing web services. Key principles:

1. **Stateless:** Each request contains all needed information
2. **Client-Server:** Separation of concerns
3. **Uniform Interface:** Standard HTTP methods (GET, POST, PUT, DELETE)
4. **Resource-Based:** URLs represent resources (e.g., `/conversations/123`)

### Q11: What HTTP status codes did you use?
**Answer:**
- **200 OK:** Successful request
- **201 Created:** Resource created successfully
- **400 Bad Request:** Invalid input (validation error)
- **404 Not Found:** Resource doesn't exist
- **500 Internal Server Error:** Server-side error

---

## ⚛️ React Questions

### Q12: What is useState in React?
**Answer:**
`useState` is a React Hook that lets you add state to functional components.

```typescript
const [messages, setMessages] = useState<Message[]>([]);
//     ↑          ↑                              ↑
//   current   function to               initial value
//   value     update value              (empty array)
```

### Q13: What is the difference between props and state?
**Answer:**
| Props | State |
|-------|-------|
| Passed from parent component | Managed within component |
| Read-only (immutable) | Can be changed (mutable) |
| Used to configure a component | Used to store dynamic data |

### Q14: What is TypeScript and why use it?
**Answer:**
TypeScript is a superset of JavaScript that adds static typing. Benefits:

1. **Type Safety:** Catches errors at compile time
2. **Better IDE Support:** Auto-completion and documentation
3. **Easier Refactoring:** IDE can find all usages
4. **Self-Documenting:** Types serve as documentation

```typescript
// TypeScript knows 'message' must have these fields
interface Message {
  id: string;
  text: string;
  sender: 'user' | 'ai';
}
```

---

## 🔄 Spring Boot Questions

### Q15: What is Spring Boot?
**Answer:**
Spring Boot is a framework that simplifies Spring application development by:

1. **Auto-Configuration:** Automatically configures based on dependencies
2. **Embedded Server:** No need to deploy WAR files
3. **Starter Dependencies:** Pre-configured dependency bundles
4. **Production Ready:** Built-in monitoring and health checks

### Q16: What is Dependency Injection?
**Answer:**
Dependency Injection (DI) is a design pattern where objects receive their dependencies from external sources rather than creating them.

```java
// WITHOUT DI (Bad - tight coupling)
public class ChatController {
    private ConversationService service = new ConversationService();
}

// WITH DI (Good - loose coupling)
public class ChatController {
    private final ConversationService service;
    
    public ChatController(ConversationService service) {
        this.service = service;  // Injected by Spring
    }
}
```

Benefits: Easier testing, loose coupling, flexibility

### Q17: What is the difference between `@RestController` and `@Controller`?
**Answer:**
- `@Controller`: Returns view names (for HTML pages)
- `@RestController`: Returns data directly as JSON (combines `@Controller` + `@ResponseBody`)

```java
@RestController  // All methods return JSON
public class ChatController {
    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");  // Returns JSON
    }
}
```

---

## 🚀 Deployment Questions

### Q18: How did you deploy this application?
**Answer:**
I deployed on Render.com using Docker:

1. Created a Dockerfile with multi-stage build
2. Connected GitHub repository to Render
3. Created PostgreSQL database on Render
4. Set environment variables (DATABASE_URL, LLM_API_KEY)
5. Render automatically builds and deploys on git push

### Q19: What is Docker and why use it?
**Answer:**
Docker is a platform for containerizing applications. A container packages the application with all its dependencies.

Benefits:
- **Consistency:** Same environment everywhere
- **Isolation:** Doesn't affect host system
- **Portability:** Runs on any system with Docker

```dockerfile
# Our Dockerfile structure
FROM maven:3.9          # Build stage
FROM eclipse-temurin:17 # Runtime stage (smaller image)
```

---

## 🤖 AI Integration Questions

### Q20: How does the AI integration work?
**Answer:**
1. User sends message → Backend receives it
2. Backend retrieves conversation history (last 10 messages)
3. Backend constructs a prompt with system message + history + user message
4. Sends POST request to Groq API
5. Parses AI response and saves to database
6. Returns response to frontend

### Q21: What is a system prompt?
**Answer:**
A system prompt is an instruction given to the AI that defines its behavior and personality. In our app:

```java
systemMessage.put("content", 
    "You are a helpful customer support assistant. Be concise and friendly.");
```

This makes the AI respond in a helpful, professional manner.

---

## 🧪 Testing Questions

### Q22: How would you test this application?
**Answer:**
1. **Unit Tests:** Test individual methods in isolation
2. **Integration Tests:** Test components working together
3. **API Tests:** Test REST endpoints with test data
4. **End-to-End Tests:** Test complete user flows

Example unit test:
```java
@Test
void testCreateConversation() {
    Conversation conv = conversationService.createConversation();
    assertNotNull(conv.getId());
    assertNotNull(conv.getCreatedAt());
}
```

---

## 💡 Problem-Solving Questions

### Q23: What challenges did you face and how did you solve them?
**Answer:**
1. **Database Connection Issues:** PostgreSQL required different dialect than MySQL. Solved by explicitly setting PostgreSQL dialect in production config.

2. **CORS Errors:** Frontend couldn't call backend API. Solved by adding `@CrossOrigin` annotation to controllers.

3. **SPA Routing:** React routes returned 404 on refresh. Solved by configuring Spring to forward unknown routes to index.html.

### Q24: How would you improve this project?
**Answer:**
1. **Add User Authentication:** Login/signup with JWT tokens
2. **Conversation History:** Let users see past conversations
3. **Multiple AI Models:** Let users choose between different AI models
4. **Rate Limiting:** Prevent API abuse
5. **WebSocket Support:** Real-time streaming responses

---

## 📚 General Programming Questions

### Q25: What is the difference between SQL and NoSQL databases?
**Answer:**
| SQL (PostgreSQL) | NoSQL (MongoDB) |
|------------------|-----------------|
| Structured tables | Flexible documents |
| Schema required | Schema optional |
| ACID compliant | Eventually consistent |
| Good for: relationships | Good for: unstructured data |

### Q26: What is an API?
**Answer:**
API (Application Programming Interface) is a set of rules that allows different software applications to communicate. In this project:
- Frontend → Backend API (REST)
- Backend → Groq API (REST)

---

## 🎤 Behavioral Questions

### Q27: Why did you build this project?
**Answer:**
"I built this project to learn full-stack development with modern technologies. I wanted to understand how to:
- Build REST APIs with Spring Boot
- Create interactive UIs with React
- Integrate third-party AI services
- Deploy applications to the cloud"

### Q28: What did you learn from this project?
**Answer:**
"I learned:
- How Spring Boot auto-configuration simplifies development
- How React components manage state and props
- How to troubleshoot deployment issues
- The importance of error handling and logging
- How AI APIs work and how to integrate them"

---

## 📝 Quick Reference Card

```
Frontend Stack:
- React 18 + TypeScript
- Vite (build tool)
- Axios (HTTP client)

Backend Stack:
- Java 17 + Spring Boot 3.2
- Spring Data JPA + Hibernate
- PostgreSQL / MySQL

Key Concepts:
- REST API
- Dependency Injection
- ORM (Object-Relational Mapping)
- React Hooks (useState, useEffect)
- TypeScript Interfaces

Architecture:
Frontend → REST API → Service Layer → Repository → Database
                   ↓
              AI Service (Groq)
```

---

Good luck with your interviews! 🍀
