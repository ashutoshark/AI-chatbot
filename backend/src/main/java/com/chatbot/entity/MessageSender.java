package com.chatbot.entity;

/**
 * Enum representing who sent a message.
 * Stored as string in database ("user" or "ai").
 */
public enum MessageSender {
    user,  // Message from the customer
    ai;    // Message from the AI assistant
    
    public boolean isAI() {
        return this == ai;
    }
    
    public boolean isUser() {
        return this == user;
    }
}
