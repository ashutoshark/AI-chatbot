package com.chatbot.dto;

import com.chatbot.entity.MessageSender;

import java.time.LocalDateTime;

/**
 * Response DTO for message history
 */
public class MessageResponse {
    
    private String id;
    private MessageSender sender;
    private String text;
    private LocalDateTime timestamp;
    
    public MessageResponse() {}
    
    public MessageResponse(String id, MessageSender sender, String text, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public MessageSender getSender() { return sender; }
    public void setSender(MessageSender sender) { this.sender = sender; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
