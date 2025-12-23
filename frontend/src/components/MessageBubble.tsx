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
    // Message row - controls alignment
    <div className={`message-row ${message.sender}`}>
      {/* The bubble itself */}
      <div className={`message-bubble ${message.sender}`}>
        {/* Sender label with icon */}
        <div className="message-sender">
          <span className="sender-icon">{message.sender === 'user' ? '👤' : '🤖'}</span>
          <span>{message.sender === 'user' ? 'You' : 'AI'}</span>
        </div>
        
        {/* Message text */}
        <div className="message-text">{message.text}</div>
        
        {/* Timestamp */}
        {message.timestamp && (
          <div className="message-time">{formatTime(message.timestamp)}</div>
        )}
      </div>
    </div>
  );
};

export default MessageBubble;
