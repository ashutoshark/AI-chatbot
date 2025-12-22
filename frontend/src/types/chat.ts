export interface Message {
  id: string;
  text: string;
  sender: 'user' | 'ai';
  timestamp: string;
}

export interface ChatResponse {
  conversationId: string;
  messageId: string;
  message: string;
  sender: string;
  timestamp: string | null;
}

export interface ChatRequest {
  conversationId?: string;
  message: string;
}

export interface Conversation {
  id: string;
  createdAt: string;
  updatedAt: string;
}
