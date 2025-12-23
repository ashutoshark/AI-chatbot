package com.chatbot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for POST /api/chat endpoint.
 * Contains user's message and optional conversation ID.
 */
public class ChatRequest {
    
    // Optional - null for first message, set for continuing conversation
    private String conversationId;
    
    @NotBlank(message = "Message cannot be blank")
    @Size(min = 1, max = 3000, message = "Message must be between 1 and 3000 characters")
    private String message;
    
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
