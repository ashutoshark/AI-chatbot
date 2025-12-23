/**
 * ============================================
 * MESSAGE BUBBLE COMPONENT
 * ============================================
 * 
 * This component displays a single chat message.
 * It shows different styles for user messages (right side, purple)
 * and AI messages (left side, gray).
 * 
 * Props:
 * - message: The message object containing id, text, sender, timestamp
 */

import React from 'react';
import type { Message } from '../types/chat';
import './MessageBubble.css';

// Define the props this component accepts
interface MessageBubbleProps {
  message: Message;
}

const MessageBubble: React.FC<MessageBubbleProps> = ({ message }) => {
  
  /**
   * Formats the timestamp to a readable time string
   * Example: "10:30 AM"
   */
  const formatTime = (timestamp: string) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString('en-US', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
  };

  return (
    // The class changes based on sender: "message-bubble user" or "message-bubble ai"
    <div className={`message-bubble ${message.sender}`}>
      {/* Sender label */}
      <div className="message-sender">
        {message.sender === 'user' ? '👤 You' : '🤖 AI'}
      </div>
      
      {/* Message text */}
      <div className="message-text">{message.text}</div>
      
      {/* Timestamp (only shown if available) */}
      {message.timestamp && (
        <div className="message-time">{formatTime(message.timestamp)}</div>
      )}
    </div>
  );
};

export default MessageBubble;
