/**
 * ============================================
 * CHAT API SERVICE
 * ============================================
 * 
 * This file handles all communication with the backend API.
 * It uses Axios library to make HTTP requests.
 * 
 * What is Axios?
 * - Axios is a popular library for making HTTP requests
 * - It's easier to use than the built-in fetch() API
 * - It automatically converts JSON data
 * 
 * API Endpoints:
 * - POST /api/chat - Send a message and get AI response
 * - POST /api/conversations - Create a new conversation
 * - GET /api/conversations/:id - Get conversation details
 * - GET /api/conversations/:id/messages - Get all messages
 * - DELETE /api/conversations/:id - Delete a conversation
 */

import axios from 'axios';
import type { ChatRequest, ChatResponse, Conversation, Message } from '../types/chat';

// Get API URL from environment variable or use localhost for development
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api';

// Create an Axios instance with default settings
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',  // We're sending JSON data
  },
});

/**
 * Chat API object containing all API methods
 * Each method corresponds to a backend endpoint
 */
export const chatApi = {
  /**
   * Send a message and get AI response
   * This is the main API call used for chatting
   * 
   * @param request - Contains conversationId (optional) and message text
   * @returns AI response with conversationId and message
   */
  sendMessage: async (request: ChatRequest): Promise<ChatResponse> => {
    const response = await api.post<ChatResponse>('/chat', request);
    return response.data;
  },

  /**
   * Create a new conversation
   * Usually not called directly - sendMessage creates one if needed
   */
  createConversation: async (): Promise<Conversation> => {
    const response = await api.post<Conversation>('/conversations');
    return response.data;
  },

  /**
   * Get conversation details by ID
   * Useful for loading a saved conversation
   */
  getConversation: async (conversationId: string): Promise<Conversation> => {
    const response = await api.get<Conversation>(`/conversations/${conversationId}`);
    return response.data;
  },

  /**
   * Get all messages in a conversation
   * Useful for loading conversation history
   */
  getMessages: async (conversationId: string): Promise<Message[]> => {
    const response = await api.get<Message[]>(`/conversations/${conversationId}/messages`);
    return response.data;
  },

  /**
   * Delete a conversation and all its messages
   * Permanently removes the conversation from database
   */
  deleteConversation: async (conversationId: string): Promise<void> => {
    await api.delete(`/conversations/${conversationId}`);
  },
};
