package com.chatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "chatbot-backend");
        return ResponseEntity.ok(response);
    }
}

@Controller
class IndexController {
    /**
     * Redirect all non-API routes to index.html for React Router
     * This allows the frontend to handle routing
     */
    @GetMapping(value = {"/", "/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/**"})
    public String index() {
        return "forward:/index.html";
    }
}
