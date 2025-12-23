package com.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

/**
 * Service that integrates with AI APIs (Groq, OpenAI, Gemini)
 * Handles LLM calls with proper error handling and rate limiting
 */
@Service
public class LlmService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${llm.api.key}")
    private String apiKey;

    @Value("${llm.provider:groq}")
    private String provider;

    @Value("${llm.groq.model:llama-3.1-8b-instant}")
    private String groqModel;

    @Value("${llm.groq.base-url:https://api.groq.com/openai/v1}")
    private String groqBaseUrl;

    @Value("${llm.openai.model:gpt-4o-mini}")
    private String openaiModel;

    @Value("${llm.gemini.model:gemini-1.5-flash}")
    private String geminiModel;

    @Value("${llm.max-tokens:500}")
    private int maxTokens;
    
    // Maximum messages to include in context (cost control)
    @Value("${llm.max-history:10}")
    private int maxHistory;

    // System prompt with e-commerce store knowledge (FAQ)
    private static final String SYSTEM_PROMPT = """
        You are a helpful customer support agent for "TechStyle Store", a small e-commerce store 
        selling electronics and accessories. Answer clearly and concisely.
        
        === STORE KNOWLEDGE / FAQ ===
        
        **About TechStyle Store:**
        - We sell electronics, gadgets, phone accessories, and tech gear
        - Founded in 2020, based in San Francisco, CA
        - Website: www.techstyle-store.com (fictional)
        
        **Shipping Policy:**
        - FREE standard shipping on orders over $50
        - Standard shipping: 5-7 business days ($4.99 for orders under $50)
        - Express shipping: 2-3 business days ($12.99)
        - Overnight shipping: Next business day ($24.99)
        - We ship to all 50 US states
        - International shipping available to Canada and UK (7-14 business days, $19.99)
        - Orders placed before 2 PM EST ship same day
        
        **Return & Refund Policy:**
        - 30-day return window from delivery date
        - Items must be unused and in original packaging
        - FREE returns on defective items
        - Return shipping fee: $5.99 for non-defective returns
        - Refunds processed within 5-7 business days after we receive the item
        - Original shipping costs are non-refundable
        - Electronics with opened seals: 15-day return window, 15% restocking fee
        
        **Support Hours:**
        - Live Chat: Monday-Friday, 9 AM - 8 PM EST
        - Email Support: support@techstyle-store.com (24-48 hour response)
        - Phone Support: 1-800-TECH-STYLE, Monday-Friday, 10 AM - 6 PM EST
        - Weekend Email Support: Limited, responses by Monday
        
        **Payment Methods:**
        - Credit/Debit Cards (Visa, MasterCard, Amex, Discover)
        - PayPal
        - Apple Pay & Google Pay
        - Afterpay (Buy now, pay later in 4 installments)
        
        **Warranty:**
        - 1-year manufacturer warranty on all electronics
        - Extended warranty available for purchase (2 or 3 years)
        - Warranty does not cover physical damage or water damage
        
        === GUIDELINES ===
        - Be friendly, professional, and helpful
        - If you don't know something specific, suggest contacting support
        - For order-specific questions, ask for the order number
        - Never make up information not in the knowledge base
        """;

    // Constructor - WebClient is used to make HTTP calls to AI APIs
    public LlmService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
        this.objectMapper = objectMapper;
    }

    // Generate AI response using conversation history for context
    public String generateResponse(List<Map<String, String>> messages, String userMessage) {
        // Validate input
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "I didn't receive a message. Could you please try again?";
        }
        
        // Truncate very long messages (cost control)
        String truncatedMessage = userMessage.length() > 2000 
            ? userMessage.substring(0, 2000) + "... [message truncated]" 
            : userMessage;
        
        // Limit history to maxHistory messages (cost control)
        List<Map<String, String>> limitedHistory = messages.size() > maxHistory 
            ? messages.subList(messages.size() - maxHistory, messages.size())
            : messages;
        
        try {
            switch (provider.toLowerCase()) {
                case "groq":
                    return callGroqApi(limitedHistory, truncatedMessage);
                case "openai":
                    return callOpenAiApi(limitedHistory, truncatedMessage);
                case "gemini":
                    return callGeminiApi(limitedHistory, truncatedMessage);
                default:
                    throw new IllegalArgumentException("Unsupported LLM provider: " + provider);
            }
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests e) {
            return "I'm receiving too many requests right now. Please wait a moment and try again.";
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized e) {
            System.err.println("LLM API authentication failed - check API key");
            return "I'm having trouble connecting to the AI service. Please contact support.";
        } catch (org.springframework.web.reactive.function.client.WebClientRequestException e) {
            // Check if it's a timeout-related error
            if (e.getCause() != null && e.getCause().getClass().getName().contains("Timeout")) {
                return "The AI service is taking too long to respond. Please try again.";
            }
            System.err.println("LLM API connection error: " + e.getMessage());
            return "I'm having trouble reaching the AI service. Please check your connection and try again.";
        } catch (Exception e) {
            System.err.println("LLM error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return "I apologize, but I'm having trouble processing your request. Please try again later.";
        }
    }

    // Call Groq API (uses OpenAI-compatible format)
    private String callGroqApi(List<Map<String, String>> messages, String userMessage) {
        // Build request body
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", groqModel);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 0.7);

        // Add messages array
        ArrayNode messagesArray = requestBody.putArray("messages");

        // System message with store knowledge (FAQ)
        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);
        messagesArray.add(systemMessage);

        // Add conversation history (limited to maxHistory)
        for (Map<String, String> msg : messages) {
            ObjectNode historyMsg = objectMapper.createObjectNode();
            historyMsg.put("role", msg.get("sender").equals("user") ? "user" : "assistant");
            historyMsg.put("content", msg.get("text"));
            messagesArray.add(historyMsg);
        }

        // Add current user message
        ObjectNode userMsg = objectMapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messagesArray.add(userMsg);

        // Make API call with timeout
        String response = webClient.post()
                .uri(groqBaseUrl + "/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(java.time.Duration.ofSeconds(30))
                .block();

        // Parse response
        return parseOpenAiResponse(response);
    }

    // Call OpenAI API (same format as Groq)
    private String callOpenAiApi(List<Map<String, String>> messages, String userMessage) {
        // Build request body
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", openaiModel);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 0.7);

        // Add messages array (same logic as Groq)
        ArrayNode messagesArray = requestBody.putArray("messages");

        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", SYSTEM_PROMPT);
        messagesArray.add(systemMessage);

        for (Map<String, String> msg : messages) {
            ObjectNode historyMsg = objectMapper.createObjectNode();
            historyMsg.put("role", msg.get("sender").equals("user") ? "user" : "assistant");
            historyMsg.put("content", msg.get("text"));
            messagesArray.add(historyMsg);
        }

        ObjectNode userMsg = objectMapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messagesArray.add(userMsg);

        // Make API call to OpenAI with timeout
        String response = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(java.time.Duration.ofSeconds(30))
                .block();

        return parseOpenAiResponse(response);
    }

    // Call Google Gemini API (different format - not yet implemented)
    private String callGeminiApi(List<Map<String, String>> messages, String userMessage) {
        throw new UnsupportedOperationException("Gemini integration coming soon!");
    }

    // Parse AI response and extract message content
    private String parseOpenAiResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
