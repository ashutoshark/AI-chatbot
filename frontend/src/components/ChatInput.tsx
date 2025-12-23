/**
 * ============================================
 * CHAT INPUT COMPONENT
 * ============================================
 * 
 * This component provides the text input area and buttons
 * for sending messages and starting new conversations.
 * 
 * Features:
 * - Multi-line text input (Shift+Enter for new line)
 * - Character count with warning colors
 * - Send button (disabled when empty or loading)
 * - New Chat button to start fresh
 * 
 * Props:
 * - onSendMessage: Function called when user sends a message
 * - onNewChat: Function called when user clicks "New Chat"
 * - disabled: Whether input is disabled (while loading)
 */

import React, { useState, type KeyboardEvent } from 'react';
import './ChatInput.css';

// Define the props this component accepts
interface ChatInputProps {
  onSendMessage: (message: string) => void;
  onNewChat: () => void;
  disabled: boolean;
}

const ChatInput: React.FC<ChatInputProps> = ({ onSendMessage, onNewChat, disabled }) => {
  // Current text in the input field
  const [message, setMessage] = useState('');
  
  // Maximum allowed characters
  const maxLength = 3000;

  /**
   * Handles sending the message
   * Trims whitespace and clears the input after sending
   */
  const handleSend = () => {
    const trimmed = message.trim();
    if (trimmed && !disabled) {
      onSendMessage(trimmed);
      setMessage('');  // Clear input after sending
    }
  };

  /**
   * Handles keyboard events
   * Enter = Send message
   * Shift+Enter = New line (default behavior)
   */
  const handleKeyPress = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();  // Prevent new line
      handleSend();
    }
  };

  /**
   * Returns the appropriate CSS class for the character count
   * Changes color based on how close to the limit
   */
  const getCharCountClass = () => {
    const length = message.length;
    if (length > maxLength) return 'char-count error';
    if (length > maxLength * 0.9) return 'char-count warning';
    return 'char-count';
  };

  return (
    <div className="chat-input-container">
      {/* Input wrapper with glow effect */}
      <div className="input-wrapper">
        {/* Text input area */}
        <textarea
          className="chat-input"
          placeholder="Type your message..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          disabled={disabled}
          rows={1}
          maxLength={maxLength + 100}
        />
        
        {/* Inline typing dots (shown when loading) */}
        {disabled && (
          <div className="inline-typing">
            <div className="inline-typing-dot"></div>
            <div className="inline-typing-dot"></div>
            <div className="inline-typing-dot"></div>
          </div>
        )}
        
        {/* Send Button */}
        <button
          className="btn-send"
          onClick={handleSend}
          disabled={disabled || !message.trim() || message.length > maxLength}
        >
          <span className="send-icon">✈️</span>
          <span>Send</span>
        </button>
      </div>
      
      {/* Footer with character count */}
      <div className="input-footer">
        <span className={getCharCountClass()}>
          {message.length} / {maxLength}
        </span>
        
        {/* New Chat Button */}
        <button
          className="btn-new-chat"
          onClick={onNewChat}
          disabled={disabled}
        >
          🔄 New Chat
        </button>
      </div>
    </div>
  );
};

export default ChatInput;
