package com.chatbot.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================
 * CONVERSATION ENTITY (Database Table: conversations)
 * ============================================
 * 
 * This class represents a chat conversation in our database.
 * Think of it as a "chat room" that contains multiple messages.
 * 
 * What is an Entity?
 * - An entity is a Java class that maps to a database table
 * - Each field in this class becomes a column in the table
 * - Each instance of this class becomes a row in the table
 * 
 * Database Relationship:
 * - One Conversation has Many Messages (1:N relationship)
 * - When we delete a conversation, all its messages are also deleted
 * 
 * @author Your Name
 * @version 1.0
 */
@Entity  // Marks this class as a database entity
@Table(name = "conversations")  // Specifies the table name
public class Conversation {
    
    /**
     * Unique identifier for each conversation
     * We use UUID (random string) instead of auto-increment numbers
     * Example: "550e8400-e29b-41d4-a716-446655440000"
     */
    @Id  // Marks this field as the primary key
    @Column(name = "id", length = 36)
    private String id;
    
    /**
     * When this conversation was created
     * This value is set automatically and never changes
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * When this conversation was last updated (new message added)
     * This value updates every time a message is added
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * List of all messages in this conversation
     * 
     * @OneToMany - One conversation has many messages
     * mappedBy = "conversation" - The Message class has a field called "conversation"
     * cascade = ALL - If we save/delete a conversation, do the same to its messages
     * orphanRemoval = true - Delete messages that are removed from the list
     */
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    /**
     * Default constructor (required by JPA/Hibernate)
     */
    public Conversation() {}
    
    /**
     * Constructor with all fields
     */
    public Conversation(String id, LocalDateTime createdAt, LocalDateTime updatedAt, List<Message> messages) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages != null ? messages : new ArrayList<>();
    }
    
    // ============================================
    // GETTERS AND SETTERS
    // ============================================
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    
    // ============================================
    // LIFECYCLE CALLBACKS (Automatic actions)
    // ============================================
    
    /**
     * Called automatically BEFORE saving a new conversation
     * Sets the creation and update timestamps
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
    
    /**
     * Called automatically BEFORE updating an existing conversation
     * Updates the "updatedAt" timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
