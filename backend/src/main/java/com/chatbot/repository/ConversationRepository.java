package com.chatbot.repository;

import com.chatbot.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Repository for Conversation entity.
 * Spring Data JPA auto-generates implementations for these methods.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    
    // Find conversations created after a date
    List<Conversation> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);
    
    // Find conversations updated after a date
    List<Conversation> findByUpdatedAtAfterOrderByUpdatedAtDesc(LocalDateTime date);
    
    // Count conversations created after date
    long countByCreatedAtAfter(LocalDateTime date);
    
    // Delete old conversations
    void deleteByCreatedAtBefore(LocalDateTime date);
}
