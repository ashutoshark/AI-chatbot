import React, { useState, type KeyboardEvent } from 'react';
import './ChatInput.css';

interface ChatInputProps {
  onSendMessage: (message: string) => void;
  onNewChat: () => void;
  disabled: boolean;
}

const ChatInput: React.FC<ChatInputProps> = ({ onSendMessage, onNewChat, disabled }) => {
  const [message, setMessage] = useState('');
  const maxLength = 3000;

  const handleSend = () => {
    const trimmed = message.trim();
    if (trimmed && !disabled) {
      onSendMessage(trimmed);
      setMessage('');
    }
  };

  const handleKeyPress = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const getCharCountClass = () => {
    const length = message.length;
    if (length > maxLength) return 'char-count error';
    if (length > maxLength * 0.9) return 'char-count warning';
    return 'char-count';
  };

  return (
    <div className="chat-input-container">
      <div className="chat-input-wrapper">
        <textarea
          className="chat-input"
          placeholder="Type your message... (Shift + Enter for new line)"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          disabled={disabled}
          rows={1}
          maxLength={maxLength + 100}
        />
        <div className="input-actions">
          <span className={getCharCountClass()}>
            {message.length} / {maxLength}
          </span>
          <div className="action-buttons">
            <button
              className="btn btn-secondary"
              onClick={onNewChat}
              disabled={disabled}
              title="Start new conversation"
            >
              🔄 New Chat
            </button>
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
