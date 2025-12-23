/**
 * ============================================
 * TYPE DEFINITIONS
 * ============================================
 * 
 * This file contains TypeScript type definitions (interfaces)
 * that define the shape of our data objects.
 * 
 * Why use TypeScript interfaces?
 * - They help catch errors at compile time (before running)
 * - They provide auto-completion in your IDE
 * - They serve as documentation for data structures
 */

/**
 * Represents a single chat message
 * Used for both user and AI messages
 */
export interface Message {
  id: string;           // Unique identifier for the message
  text: string;         // The actual message content
  sender: 'user' | 'ai'; // Who sent the message
  timestamp: string;    // When the message was sent (ISO format)
}

/**
 * Response from the /api/chat endpoint
 * Contains the AI's response and conversation info
 */
export interface ChatResponse {
  conversationId: string;  // ID of the conversation
  messageId: string;       // ID of the AI's message
  message: string;         // The AI's response text
  sender: string;          // Always "ai" for responses
  timestamp: string | null; // When the response was generated
}

/**
 * Request body for the /api/chat endpoint
 * Sent when user sends a message
 */
export interface ChatRequest {
  conversationId?: string; // Optional - omit for first message
  message: string;         // The user's message text
}

/**
 * Represents a conversation session
 * Contains metadata but not the messages themselves
 */
export interface Conversation {
  id: string;          // Unique identifier for the conversation
  createdAt: string;   // When the conversation started
  updatedAt: string;   // Last activity time
}
