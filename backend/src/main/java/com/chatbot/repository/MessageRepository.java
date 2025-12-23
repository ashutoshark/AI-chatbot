package com.chatbot.repository;

import com.chatbot.entity.Message;
import com.chatbot.entity.MessageSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for Message entity.
 * Provides methods for querying messages by conversation.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    
    // Get all messages in conversation (oldest first)
    List<Message> findByConversation_IdOrderByCreatedAtAsc(String conversationId);
    
    // Get recent messages with pagination
    Page<Message> findByConversation_IdOrderByCreatedAtDesc(String conversationId, Pageable pageable);
    
    // Get messages by sender
    List<Message> findByConversation_IdAndSenderOrderByCreatedAtAsc(String conversationId, MessageSender sender);
    
    // Count messages in conversation
    long countByConversation_Id(String conversationId);
    
    // Get top 10 recent messages
    List<Message> findTop10ByConversation_IdOrderByCreatedAtDesc(String conversationId);
    
    // Count messages by sender
    long countByConversation_IdAndSender(String conversationId, MessageSender sender);
}
