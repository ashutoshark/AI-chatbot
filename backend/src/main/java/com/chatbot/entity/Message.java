package com.chatbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * ============================================
 * MESSAGE ENTITY (Database Table: messages)
 * ============================================
 * 
 * This class represents a single chat message in our database.
 * A message can be from the USER or from the AI.
 * 
 * Database Relationship:
 * - Many Messages belong to One Conversation (N:1 relationship)
 * - A message cannot exist without a conversation
 * 
 * Example row in database:
 * | id       | conversation_id | sender | text           | created_at          |
 * | abc-123  | conv-456        | user   | Hello!         | 2024-01-15 10:30:00 |
 * | def-789  | conv-456        | ai     | Hi! How can I..| 2024-01-15 10:30:02 |
 * 
 * @author Your Name
 * @version 1.0
 */
@Entity  // Marks this class as a database entity
@Table(name = "messages", indexes = {
    // Index helps the database find messages faster when searching by conversation and time
    @Index(name = "idx_conversation_created", columnList = "conversation_id, created_at")
})
public class Message {
    
    /**
     * Unique identifier for each message (UUID format)
     * Example: "550e8400-e29b-41d4-a716-446655440000"
     */
    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;
    
    /**
     * The conversation this message belongs to
     * 
     * @ManyToOne - Many messages can belong to one conversation
     * FetchType.LAZY - Don't load the conversation until we actually need it (better performance)
     * @JsonIgnore - Don't include conversation data when converting to JSON (prevents infinite loop)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonIgnore
    private Conversation conversation;
    
    /**
     * Who sent this message: "user" or "ai"
     * 
     * @Enumerated(STRING) - Store the enum value as text in database ("user" or "ai")
     * instead of numbers (0 or 1)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "sender", nullable = false)
    private MessageSender sender;
    
    /**
     * The actual message content
     * columnDefinition = "TEXT" - Allows for longer messages (not limited to 255 chars)
     */
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;
    
    /**
     * When this message was sent
     * updatable = false - This value never changes after creation
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // ============================================
    // CONSTRUCTORS
    // ============================================
    
    /**
     * Default constructor (required by JPA/Hibernate)
     */
    public Message() {}
    
    /**
     * Constructor with all fields
     */
    public Message(String id, Conversation conversation, MessageSender sender, String text, LocalDateTime createdAt) {
        this.id = id;
        this.conversation = conversation;
        this.sender = sender;
        this.text = text;
        this.createdAt = createdAt;
    }
    
    // ============================================
    // GETTERS AND SETTERS
    // ============================================
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public Conversation getConversation() { return conversation; }
    public void setConversation(Conversation conversation) { this.conversation = conversation; }
    
    public MessageSender getSender() { return sender; }
    public void setSender(MessageSender sender) { this.sender = sender; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================
    
    /**
     * Called automatically BEFORE saving a new message
     * Sets the creation timestamp to current time
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
