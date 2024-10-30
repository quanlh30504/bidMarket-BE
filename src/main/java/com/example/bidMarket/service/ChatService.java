package com.example.bidMarket.service;

import com.example.bidMarket.dto.ChatMessageDto;
import com.example.bidMarket.dto.ChatRoomDto;
import com.example.bidMarket.dto.Request.ChatMessageRequest;
import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.Enum.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatMessageDto sendMessage(ChatMessageRequest request, String senderId);
    Page<ChatMessageDto> getRoomMessages(UUID roomId, Pageable pageable);
    List<ChatRoomDto> getUserRooms(UUID userId);
    ChatRoomDto getOrCreateChatRoom(UUID user1Id, UUID user2Id);
}