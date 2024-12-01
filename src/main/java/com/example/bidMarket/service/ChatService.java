package com.example.bidMarket.service;

import com.example.bidMarket.Enum.MessageStatus;
import com.example.bidMarket.dto.ChatRoomDTO;
import com.example.bidMarket.dto.MessageDTO;
import com.example.bidMarket.dto.Request.MessageRequest;
import com.example.bidMarket.dto.UserDto;
import com.example.bidMarket.model.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ChatService {

    List<ChatRoomDTO> getUserRooms(String username);

    Page<MessageDTO> getRoomMessages(UUID roomId, String username, int page, int size);

    Page<MessageDTO> getMessagesBefore(UUID roomId, String username, LocalDateTime timestamp, int page, int size);

    Long getNewMessageCount(UUID roomId, String username, LocalDateTime since);

    MessageDTO sendMessage(String username, UUID roomId, @Valid MessageRequest message);

    void updateMessageStatus(UUID roomId, String userId, List<UUID> messageIds, MessageStatus status);

    ChatRoomDTO getOrCreateRoom(String userId, String otherUserId);

    void deleteRoom(UUID roomId, String userId);
}