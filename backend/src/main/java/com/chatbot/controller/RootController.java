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
     * Redirect all non-API, non-file routes to index.html for React Router.
     * Excludes any path containing a dot (assets like .js, .css, .ico) to avoid forward loops.
     */
        @GetMapping(value = {
            "/",
            "/{path:^(?!api$)(?!assets$)(?!static$)(?!webjars$)(?!favicon\\.ico$)(?!robots\\.txt$)[^\\.]*$}",
            "/{path:^(?!api$)(?!assets$)(?!static$)(?!webjars$)(?!favicon\\.ico$)(?!robots\\.txt$)[^\\.]*$}/**"
        })
    public String index() {
        return "forward:/index.html";
    }
}
