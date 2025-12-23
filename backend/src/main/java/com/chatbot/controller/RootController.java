package com.chatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Root controller for health check endpoint.
 * Used by Render.com to verify the service is running.
 */
@RestController
public class RootController {

    /** Health check endpoint - returns service status */
    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "chatbot-backend");
        return ResponseEntity.ok(response);
    }
}
