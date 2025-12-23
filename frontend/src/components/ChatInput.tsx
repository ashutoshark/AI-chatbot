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
      <div className="chat-input-wrapper">
        {/* Text input area */}
        <textarea
          className="chat-input"
          placeholder="Type your message... (Shift + Enter for new line)"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          disabled={disabled}
          rows={1}
          maxLength={maxLength + 100}  // Allow slight overflow for better UX
        />
        
        {/* Bottom bar with character count and buttons */}
        <div className="input-actions">
          <span className={getCharCountClass()}>
            {message.length} / {maxLength}
          </span>
          
          <div className="action-buttons">
            {/* New Chat Button */}
            <button
              className="btn btn-secondary"
              onClick={onNewChat}
              disabled={disabled}
              title="Start new conversation"
            >
              🔄 New Chat
            </button>
            
            {/* Send Button */}
            <button
              className="btn btn-primary"
              onClick={handleSend}
              disabled={disabled || !message.trim() || message.length > maxLength}
            >
              {disabled ? '⏳ Sending...' : '📤 Send'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatInput;
