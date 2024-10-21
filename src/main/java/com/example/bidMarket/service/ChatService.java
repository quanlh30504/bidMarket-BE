package com.example.bidMarket.service;

import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.Enum.MessageType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatMessage saveAndSendTextMessage(ChatMessage message);
    ChatMessage saveAndSendFileMessage(UUID senderId, UUID receiverId, MultipartFile file, MessageType type) throws IOException;
    List<ChatMessage> getConversationHistory(UUID user1Id, UUID user2Id);
}
