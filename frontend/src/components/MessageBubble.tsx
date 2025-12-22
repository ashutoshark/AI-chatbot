import React from 'react';
import type { Message } from '../types/chat';
import './MessageBubble.css';

interface MessageBubbleProps {
  message: Message;
}

const MessageBubble: React.FC<MessageBubbleProps> = ({ message }) => {
  const formatTime = (timestamp: string) => {
    const date = new Date(timestamp);
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
  };

  return (
    <div className={`message-bubble ${message.sender}`}>
      <div className="message-sender">{message.sender === 'user' ? 'You' : 'AI'}</div>
      <div className="message-text">{message.text}</div>
      {message.timestamp && (
        <div className="message-time">{formatTime(message.timestamp)}</div>
      )}
    </div>
  );
};

export default MessageBubble;
