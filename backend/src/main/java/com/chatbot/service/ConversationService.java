package com.chatbot.service;

import com.chatbot.entity.Conversation;
import com.chatbot.entity.Message;
import com.chatbot.entity.MessageSender;
import com.chatbot.repository.ConversationRepository;
import com.chatbot.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing conversations and messages
 */
@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final LlmService llmService;
    
    public ConversationService(ConversationRepository conversationRepository, 
                             MessageRepository messageRepository, 
                             LlmService llmService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.llmService = llmService;
    }

    // Create a new conversation
    @Transactional
    public Conversation createConversation() {
        Conversation conversation = new Conversation();
        conversation.setId(UUID.randomUUID().toString());
        return conversationRepository.save(conversation);
    }

    // Get conversation by ID
    public Conversation getConversation(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found: " + conversationId));
    }

    // Get all messages in a conversation (ordered by time)
    public List<Message> getMessages(String conversationId) {
        getConversation(conversationId);
        return messageRepository.findByConversation_IdOrderByCreatedAtAsc(conversationId);
    }

    // Send user message and get AI response
    // Steps: save user msg -> get history -> call AI -> save AI msg -> return
    @Transactional
    public Message sendMessage(String conversationId, String userMessageText) {
        // 1. Verify conversation exists
        Conversation conversation = getConversation(conversationId);

        // 2. Save user message
        Message userMessage = new Message();
        userMessage.setId(UUID.randomUUID().toString());
        userMessage.setConversation(conversation);
        userMessage.setSender(MessageSender.user);
        userMessage.setText(userMessageText);
        messageRepository.save(userMessage);

        // 3. Get conversation history (last 10 messages for context)
        List<Message> history = messageRepository.findTop10ByConversation_IdOrderByCreatedAtDesc(conversationId);
        
        // Convert to format expected by LLM service
        List<Map<String, String>> historyForLlm = history.stream()
                .map(msg -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("sender", msg.getSender().name().toLowerCase());
                    map.put("text", msg.getText());
                    return map;
                })
                .collect(Collectors.toList());

        // 4. Generate AI response
        String aiResponseText = llmService.generateResponse(historyForLlm, userMessageText);

        // 5. Save AI response
        Message aiMessage = new Message();
        aiMessage.setId(UUID.randomUUID().toString());
        aiMessage.setConversation(conversation);
        aiMessage.setSender(MessageSender.ai);
        aiMessage.setText(aiResponseText);
        messageRepository.save(aiMessage);

        // 6. Return AI message
        return aiMessage;
    }

    // Delete conversation and all its messages
    @Transactional
    public void deleteConversation(String conversationId) {
        getConversation(conversationId);
        conversationRepository.deleteById(conversationId);
    }
}
