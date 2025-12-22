package com.chatbot.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for conversation details
 */
public class ConversationResponse {
    
    private String id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ConversationResponse() {}
    
    public ConversationResponse(String id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
