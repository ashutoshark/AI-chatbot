package com.chatbot.controller;

import com.chatbot.dto.ChatRequest;
import com.chatbot.dto.ChatResponse;
import com.chatbot.dto.ConversationResponse;
import com.chatbot.dto.MessageResponse;
import com.chatbot.entity.Conversation;
import com.chatbot.entity.Message;
import com.chatbot.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for chat API endpoints
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ConversationService conversationService;
    
    public ChatController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // POST /api/chat - Send message and get AI response
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        
        // 1. Get or create conversation
        String conversationId = request.getConversationId();
        if (conversationId == null || conversationId.isBlank()) {
            // First message - create new conversation
            Conversation newConversation = conversationService.createConversation();
            conversationId = newConversation.getId();
        }

        // 2. Send message and get AI response
        Message aiMessage = conversationService.sendMessage(conversationId, request.getMessage());

        // 3. Build response DTO
        ChatResponse response = new ChatResponse(
            conversationId,
            aiMessage.getId(),
            aiMessage.getText(),
            aiMessage.getSender().name().toLowerCase(),
            aiMessage.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    // POST /api/conversations - Create new conversation
    @PostMapping("/conversations")
    public ResponseEntity<ConversationResponse> createConversation() {
        Conversation conversation = conversationService.createConversation();
        
        ConversationResponse response = new ConversationResponse(
            conversation.getId(),
            conversation.getCreatedAt(),
            conversation.getUpdatedAt()
        );
        
        return ResponseEntity.ok(response);
    }

    // GET /api/conversations/{id} - Get conversation details
    @GetMapping("/conversations/{id}")
    public ResponseEntity<ConversationResponse> getConversation(@PathVariable String id) {
        Conversation conversation = conversationService.getConversation(id);
        
        ConversationResponse response = new ConversationResponse(
            conversation.getId(),
            conversation.getCreatedAt(),
            conversation.getUpdatedAt()
        );
        
        return ResponseEntity.ok(response);
    }

    // GET /api/conversations/{id}/messages - Get all messages
    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable String id) {
        List<Message> messages = conversationService.getMessages(id);
        
        List<MessageResponse> response = messages.stream()
            .map(msg -> new MessageResponse(
                msg.getId(),
                msg.getSender(),
                msg.getText(),
                msg.getCreatedAt()
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    // DELETE /api/conversations/{id} - Delete conversation
    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable String id) {
        conversationService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }
}
