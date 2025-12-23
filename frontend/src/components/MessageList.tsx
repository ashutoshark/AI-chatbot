/**
 * ============================================
 * MESSAGE LIST COMPONENT
 * ============================================
 * 
 * This component displays the list of all messages in the conversation.
 * It also shows:
 * - Empty state when no messages exist
 * - Typing indicator when AI is generating a response
 * - Loading state when fetching conversation history
 * - Auto-scrolls to the latest message
 * 
 * Props:
 * - messages: Array of all messages to display
 * - isLoading: Whether the AI is currently generating a response
 * - isLoadingHistory: Whether we're loading previous conversation
 */

import React, { useEffect, useRef } from 'react';
import MessageBubble from './MessageBubble';
import type { Message } from '../types/chat';
import './MessageList.css';

// Define the props this component accepts
interface MessageListProps {
  messages: Message[];
  isLoading: boolean;
  isLoadingHistory?: boolean;
}

const MessageList: React.FC<MessageListProps> = ({ messages, isLoading, isLoadingHistory = false }) => {
  // Reference to the bottom of the message list (for auto-scrolling)
  const messagesEndRef = useRef<HTMLDivElement>(null);

  /**
   * Smoothly scrolls to the bottom of the message list
   */
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  // Auto-scroll whenever messages change or loading state changes
  useEffect(() => {
    scrollToBottom();
  }, [messages, isLoading]);

  // Show loading state when fetching conversation history
  if (isLoadingHistory) {
    return (
      <div className="message-list">
        <div className="empty-state">
          <div className="empty-state-icon loading-spin">⏳</div>
          <div className="empty-state-text">Loading conversation...</div>
          <div className="empty-state-subtext">
            Please wait while we fetch your previous messages
          </div>
        </div>
      </div>
    );
  }

  // Show empty state when there are no messages
  if (messages.length === 0 && !isLoading) {
    return (
      <div className="message-list">
        <div className="empty-state">
          <div className="empty-state-icon">🛍️</div>
          <div className="empty-state-text">Welcome to ShopEase Support!</div>
          <div className="empty-state-subtext">
            Ask me about shipping, returns, orders, or anything else!
          </div>
          <div className="empty-state-suggestions">
            <span className="suggestion">📦 Shipping info</span>
            <span className="suggestion">↩️ Return policy</span>
            <span className="suggestion">⏰ Support hours</span>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="message-list">
      {/* Render each message */}
      {messages.map((message) => (
        <MessageBubble key={message.id} message={message} />
      ))}
      
      {/* Show typing indicator while waiting for AI response */}
      {isLoading && (
        <div className="typing-indicator">
          <div className="typing-bubble">
            <div className="typing-label">
              <span>🤖</span>
              <span>AI</span>
            </div>
            <div className="typing-dots">
              <div className="typing-dot"></div>
              <div className="typing-dot"></div>
              <div className="typing-dot"></div>
            </div>
          </div>
        </div>
      )}
      
      {/* Invisible element at the bottom for scrolling */}
      <div ref={messagesEndRef} />
    </div>
  );
};

export default MessageList;
