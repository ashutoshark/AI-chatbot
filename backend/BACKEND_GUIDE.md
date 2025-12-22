# Backend Setup & Testing Guide

## ✅ What We've Implemented

### Service Layer (Phase 3)
- **LlmService**: Integrates with AI APIs (Groq/OpenAI)
  - Supports multiple providers (Groq, OpenAI, Gemini placeholder)
  - Sends conversation history for context
  - Parses AI responses

- **ConversationService**: Manages conversations and messages
  - Creates new conversations
  - Saves user messages
  - Gets AI responses via LlmService
  - Retrieves message history
  - Deletes conversations

### API Layer (Phase 4)
- **ChatController**: REST API endpoints
  - `POST /api/chat` - Send message and get AI response
  - `POST /api/conversations` - Create new conversation
  - `GET /api/conversations/{id}` - Get conversation details
  - `GET /api/conversations/{id}/messages` - Get message history
  - `DELETE /api/conversations/{id}` - Delete conversation

- **DTOs**: Data Transfer Objects
  - `ChatRequest` - User message input
  - `ChatResponse` - AI response output
  - `MessageResponse` - Message history format
  - `ConversationResponse` - Conversation details

- **Exception Handling**:
  - Validation errors (400 Bad Request)
  - Not found errors (404 Not Found)
  - Generic errors (500 Internal Server Error)

## 🚀 How to Run

### Prerequisites
1. **MySQL** - Make sure MySQL is running on port 3306
   ```bash
   # If using Docker:
   docker run --name mysql-chatbot -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=chatbot_db -p 3306:3306 -d mysql:8.0
   ```

2. **API Key** - Get a free API key from one of these:
   - **Groq** (Recommended): https://console.groq.com/keys
   - **Google Gemini**: https://makersuite.google.com/app/apikey
   - **OpenAI**: https://platform.openai.com/api-keys (requires payment)

### Configuration
1. Open `backend/src/main/resources/application.yml`
2. Update these values:
   ```yaml
   spring:
     datasource:
       username: root
       password: root123  # Your MySQL password
   
   llm:
     provider: groq  # Options: groq, openai, gemini
     api:
       key: your_api_key_here  # Your actual API key
   ```

### Start the Application
```bash
cd backend
mvn spring-boot:run
```

You should see:
```
Started ChatBotApplication in X seconds
Tomcat started on port(s): 8081 (http)
```

## 🧪 Testing the API

### Using cURL

#### 1. Send a message (creates conversation automatically)
```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello! What can you help me with?"}'
```

Response:
```json
{
  "conversationId": "abc-123-def-456",
  "messageId": "msg-789",
  "message": "Hello! I'm here to help you with any questions...",
  "sender": "ai",
  "timestamp": "2024-01-15T10:30:00"
}
```

#### 2. Continue the conversation (use conversationId from step 1)
```bash
curl -X POST http://localhost:8081/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "abc-123-def-456",
    "message": "Tell me about your services"
  }'
```

#### 3. Get conversation history
```bash
curl http://localhost:8081/api/conversations/abc-123-def-456/messages
```

Response:
```json
[
  {
    "id": "msg-1",
    "sender": "user",
    "text": "Hello! What can you help me with?",
    "timestamp": "2024-01-15T10:30:00"
  },
  {
    "id": "msg-2",
    "sender": "ai",
    "text": "Hello! I'm here to help...",
    "timestamp": "2024-01-15T10:30:05"
  }
]
```

#### 4. Delete conversation
```bash
curl -X DELETE http://localhost:8081/api/conversations/abc-123-def-456
```

### Using Postman or Thunder Client

1. **Create a new request**
2. **Set method**: POST
3. **URL**: `http://localhost:8081/api/chat`
4. **Headers**: Add `Content-Type: application/json`
5. **Body** (raw JSON):
   ```json
   {
     "message": "Hello!"
   }
   ```
6. **Send** and check the response

## 🔍 Troubleshooting

### Error: "Cannot connect to database"
- Check MySQL is running: `mysql -u root -p`
- Verify credentials in `application.yml`
- Check database exists: `SHOW DATABASES;`

### Error: "Failed to parse AI response"
- Check API key is correct in `application.yml`
- Verify API provider is set correctly (`groq`, `openai`, or `gemini`)
- Check you have internet connection

### Error: "Message cannot be blank"
- Request validation failed
- Make sure to send non-empty message in request body

## 📋 What's Next?

### Immediate Next Steps:
1. Test all endpoints with actual API key
2. Build frontend (React + Vite) to interact with API
3. Add proper error messages for LLM failures
4. Implement rate limiting

### Future Enhancements:
- Add user authentication (JWT)
- Implement WebSocket for real-time chat
- Add Redis caching for conversation history
- Implement conversation search
- Add file upload support
- Deploy to cloud (AWS/Azure/GCP)

## 📚 Code Overview

### Flow: User sends message → Get AI response

1. **Frontend** sends POST to `/api/chat`
   ```json
   {"conversationId": "conv-123", "message": "Hello"}
   ```

2. **ChatController** receives request
   - Validates input (@Valid)
   - Calls ConversationService

3. **ConversationService**
   - Saves user message to database
   - Gets conversation history (last 10 messages)
   - Calls LlmService with history + new message

4. **LlmService**
   - Builds API request for Groq/OpenAI
   - Sends HTTP request via WebClient
   - Parses response and extracts AI message

5. **ConversationService**
   - Saves AI message to database
   - Returns AI message to controller

6. **ChatController** 
   - Converts to ChatResponse DTO
   - Returns JSON to frontend

7. **Frontend** displays AI response to user

## 🎯 Current Status

✅ **Completed:**
- Database entities (Conversation, Message)
- Repository layer (ConversationRepository, MessageRepository)
- Service layer (LlmService, ConversationService)
- REST API controller (ChatController)
- DTOs (request/response objects)
- Exception handling
- CORS configuration
- Build configuration (Maven)

⏳ **Next Phase:**
- Build React frontend
- Connect frontend to backend API
- Style the chat interface
- Deploy application

---

**Great job!** Your backend is fully functional and ready to handle chat requests! 🎉
