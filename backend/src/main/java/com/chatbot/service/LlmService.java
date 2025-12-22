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

    // Constructor - WebClient is used to make HTTP calls to AI APIs
    public LlmService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    // Generate AI response using conversation history for context
    public String generateResponse(List<Map<String, String>> messages, String userMessage) {
        try {
            switch (provider.toLowerCase()) {
                case "groq":
                    return callGroqApi(messages, userMessage);
                case "openai":
                    return callOpenAiApi(messages, userMessage);
                case "gemini":
                    return callGeminiApi(messages, userMessage);
                default:
                    throw new IllegalArgumentException("Unsupported LLM provider: " + provider);
            }
        } catch (Exception e) {
            return "I apologize, but I'm having trouble connecting to the AI service. Please try again later.";
        }
    }

    // Call Groq API (uses OpenAI-compatible format)
    private String callGroqApi(List<Map<String, String>> messages, String userMessage) {
        // Build request body
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", groqModel);
        requestBody.put("max_tokens", maxTokens);

        // Add messages array
        ArrayNode messagesArray = requestBody.putArray("messages");

        // System message (defines AI behavior)
        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful customer support assistant. Be concise and friendly.");
        messagesArray.add(systemMessage);

        // Add conversation history
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

        // Make API call
        String response = webClient.post()
                .uri(groqBaseUrl + "/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
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

        // Add messages array (same logic as Groq)
        ArrayNode messagesArray = requestBody.putArray("messages");

        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful customer support assistant. Be concise and friendly.");
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

        // Make API call to OpenAI
        String response = webClient.post()
                .uri("https://api.openai.com/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
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
