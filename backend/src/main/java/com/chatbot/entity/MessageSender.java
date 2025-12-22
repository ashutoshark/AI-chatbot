package com.chatbot.entity;

/**
 * Enum for message sender type
 */
public enum MessageSender {
    user,
    ai;
    
    public boolean isAI() {
        return this == ai;
    }
    
    public boolean isUser() {
        return this == user;
    }
}
