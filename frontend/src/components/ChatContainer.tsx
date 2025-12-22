import React, { useState } from 'react';
import MessageList from './MessageList';
import ChatInput from './ChatInput';
import { chatApi } from '../services/chatApi';
import type { Message } from '../types/chat';
import './ChatContainer.css';

const ChatContainer: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [conversationId, setConversationId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSendMessage = async (text: string) => {
    if (!text.trim()) return;

    const userMessage: Message = {
      id: Date.now().toString(),
      text,
      sender: 'user',
      timestamp: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setIsLoading(true);
    setError(null);

    try {
      const response = await chatApi.sendMessage({
        conversationId: conversationId || undefined,
        message: text,
      });

      if (!conversationId) {
        setConversationId(response.conversationId);
      }

      const aiMessage: Message = {
        id: response.messageId,
        text: response.message,
        sender: 'ai',
        timestamp: response.timestamp || new Date().toISOString(),
      };

      setMessages((prev) => [...prev, aiMessage]);
    } catch (err) {
      setError('Failed to send message. Please check if the backend is running.');
      console.error('Error sending message:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNewChat = () => {
    setMessages([]);
    setConversationId(null);
    setError(null);
  };

  return (
    <div className="chat-container">
      <div className="chat-header">
        <div className="header-content">
          <h1 className="chat-title">
            🤖 AI Chatbot Assistant
          </h1>
          <p className="chat-subtitle">Powered by Groq AI • llama-3.1-8b-instant</p>
        </div>
        <div className="status-indicator">
          <span className="status-dot"></span>
          <span>Online</span>
        </div>
      </div>

      {error && (
        <div className="error-message">
          <span className="error-icon">⚠️</span>
          <span>{error}</span>
        </div>
      )}

      <MessageList messages={messages} isLoading={isLoading} />
      <ChatInput
        onSendMessage={handleSendMessage}
        onNewChat={handleNewChat}
        disabled={isLoading}
      />
    </div>
  );
};

export default ChatContainer;
