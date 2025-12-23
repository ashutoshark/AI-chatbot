/**
 * ============================================
 * CHAT CONTAINER COMPONENT
 * ============================================
 * 
 * This is the main component that holds the entire chat interface.
 * It manages:
 * - The list of messages (state)
 * - Sending new messages to the backend
 * - Displaying errors
 * - Starting new conversations
 * - Session persistence (saves conversationId to localStorage)
 * 
 * Component Structure:
 * ChatContainer
 *   ├── Header (title + status)
 *   ├── MessageList (displays all messages)
 *   └── ChatInput (text input + buttons)
 */

import React, { useState, useEffect } from 'react';
import MessageList from './MessageList';
import ChatInput from './ChatInput';
import { chatApi } from '../services/chatApi';
import type { Message } from '../types/chat';
import './ChatContainer.css';

// Key for storing conversation ID in browser localStorage
const STORAGE_KEY = 'shopease_conversation_id';

const ChatContainer: React.FC = () => {
  // ============================================
  // STATE VARIABLES
  // ============================================
  
  // Array of all messages in the current conversation
  const [messages, setMessages] = useState<Message[]>([]);
  
  // ID of the current conversation (null if no conversation started)
  const [conversationId, setConversationId] = useState<string | null>(null);
  
  // True when waiting for AI response
  const [isLoading, setIsLoading] = useState(false);
  
  // True when loading conversation history
  const [isLoadingHistory, setIsLoadingHistory] = useState(true);
  
  // Error message to display (null if no error)
  const [error, setError] = useState<string | null>(null);

  // ============================================
  // LOAD CONVERSATION HISTORY ON PAGE LOAD
  // ============================================
  
  useEffect(() => {
    const loadConversationHistory = async () => {
      // Check if we have a saved conversation ID
      const savedConversationId = localStorage.getItem(STORAGE_KEY);
      
      if (savedConversationId) {
        try {
          // Try to load messages from the saved conversation
          const loadedMessages = await chatApi.getMessages(savedConversationId);
          
          if (loadedMessages && loadedMessages.length > 0) {
            // Convert backend messages to frontend format
            const formattedMessages: Message[] = loadedMessages.map((msg: any) => ({
              id: msg.id,
              text: msg.text,
              sender: msg.sender.toLowerCase() as 'user' | 'ai',
              timestamp: msg.timestamp || msg.createdAt,
            }));
            
            setMessages(formattedMessages);
            setConversationId(savedConversationId);
          }
        } catch (err) {
          // Conversation might have been deleted, clear storage
          console.log('Could not load previous conversation, starting fresh');
          localStorage.removeItem(STORAGE_KEY);
        }
      }
      
      setIsLoadingHistory(false);
    };
    
    loadConversationHistory();
  }, []);
  
  // Save conversation ID to localStorage whenever it changes
  useEffect(() => {
    if (conversationId) {
      localStorage.setItem(STORAGE_KEY, conversationId);
    }
  }, [conversationId]);

  // ============================================
  // EVENT HANDLERS
  // ============================================

  /**
   * Handles sending a message
   * 1. Add user message to the list immediately (for instant feedback)
   * 2. Send message to backend API
   * 3. Add AI response to the list when received
   */
  const handleSendMessage = async (text: string) => {
    // Don't send empty messages
    if (!text.trim()) return;

    // Create user message object
    const userMessage: Message = {
      id: Date.now().toString(),  // Temporary ID
      text,
      sender: 'user',
      timestamp: new Date().toISOString(),
    };

    // Add user message to the list immediately
    setMessages((prev) => [...prev, userMessage]);
    setIsLoading(true);
    setError(null);

    try {
      // Send message to backend and wait for AI response
      const response = await chatApi.sendMessage({
        conversationId: conversationId || undefined,
        message: text,
      });

      // Save conversation ID (important for continuing the conversation)
      if (!conversationId) {
        setConversationId(response.conversationId);
      }

      // Create AI message object from response
      const aiMessage: Message = {
        id: response.messageId,
        text: response.message,
        sender: 'ai',
        timestamp: response.timestamp || new Date().toISOString(),
      };

      // Add AI message to the list
      setMessages((prev) => [...prev, aiMessage]);
    } catch (err) {
      // Show error message if something went wrong
      setError('Failed to send message. Please check if the backend is running.');
      console.error('Error sending message:', err);
    } finally {
      // Stop showing loading indicator
      setIsLoading(false);
    }
  };

  /**
   * Starts a new conversation
   * Clears all messages and resets the conversation ID
   */
  const handleNewChat = () => {
    setMessages([]);
    setConversationId(null);
    setError(null);
    // Clear saved conversation from localStorage
    localStorage.removeItem(STORAGE_KEY);
  };

  // ============================================
  // RENDER UI
  // ============================================

  return (
    <div className="chat-container">
      {/* Header Section */}
      <div className="chat-header">
        <div className="header-content">
          {/* Robot Icon */}
          <div className="header-icon">🛒</div>
          {/* Title and Subtitle */}
          <div>
            <h1 className="chat-title">ShopEase Support</h1>
            <p className="chat-subtitle">Your e-commerce assistant • Ask about orders, shipping & more</p>
          </div>
        </div>
        {/* Online Status */}
        <div className="status-indicator">
          <span className="status-dot"></span>
          <span>Online</span>
        </div>
      </div>

      {/* Error Message (only shown when there's an error) */}
      {error && (
        <div className="error-message">
          <span className="error-icon">⚠️</span>
          <span>{error}</span>
        </div>
      )}

      {/* Message List Component */}
      <MessageList 
        messages={messages} 
        isLoading={isLoading} 
        isLoadingHistory={isLoadingHistory}
      />
      
      {/* Chat Input Component */}
      <ChatInput
        onSendMessage={handleSendMessage}
        onNewChat={handleNewChat}
        disabled={isLoading || isLoadingHistory}
      />
    </div>
  );
};

export default ChatContainer;
