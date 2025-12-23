package com.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================
 * AI CHATBOT APPLICATION - Main Entry Point
 * ============================================
 * 
 * This is the main class that starts our Spring Boot application.
 * 
 * What is Spring Boot?
 * - Spring Boot is a framework that makes it easy to create web applications
 * - It automatically configures many things for us (like database connections, web server, etc.)
 * 
 * What does @SpringBootApplication do?
 * - It tells Spring to scan this package and sub-packages for components
 * - It enables auto-configuration (Spring sets up things automatically)
 * - It marks this as a configuration class
 * 
 * @author Your Name
 * @version 1.0
 */
@SpringBootApplication
public class ChatBotApplication {

    /**
     * Main method - this is where the application starts
     * 
     * @param args Command line arguments (we don't use them in this app)
     */
    public static void main(String[] args) {
        // This single line starts the entire application!
        // Spring Boot will:
        // 1. Start the embedded web server (Tomcat)
        // 2. Connect to the database
        // 3. Set up all our controllers, services, and repositories
        SpringApplication.run(ChatBotApplication.class, args);
    }
}
