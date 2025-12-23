package com.chatbot.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for POST /api/chat endpoint.
 * Contains AI's response and conversation metadata.
 */
public class ChatResponse {
    
    private String conversationId;  // Use this for subsequent messages
    private String messageId;
    private String message;
    private String sender;
    private LocalDateTime timestamp;
    
    public ChatResponse() {}
    
    public ChatResponse(String conversationId, String messageId, String message, String sender, LocalDateTime timestamp) {
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }
    
    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }
    
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
