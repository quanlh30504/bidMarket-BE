package com.example.bidMarket.service.impl;

import com.example.bidMarket.AWS.AmazonS3Service;
import com.example.bidMarket.dto.ChatMessageDto;
import com.example.bidMarket.dto.ChatRoomDto;
import com.example.bidMarket.dto.Request.ChatMessageRequest;
import com.example.bidMarket.mapper.ChatMapper;
import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.Enum.MessageType;
import com.example.bidMarket.model.ChatRoom;
import com.example.bidMarket.repository.ChatMessageRepository;
import com.example.bidMarket.repository.ChatRoomRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ChatMessageDto sendMessage(ChatMessageRequest request, String principalId) {
        ChatRoom room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        UUID senderId = UUID.fromString(principalId);
        validateUserInRoom(room, senderId);

        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setSender(userRepository.getReferenceById(senderId));
        message.setContent(request.getContent());
        message.setType(request.getType());
        message.setFileUrl(request.getFileUrl());
        message.setTimestamp(LocalDateTime.now());

        message = messageRepository.save(message);
        ChatMessageDto messageDto = chatMapper.toDto(message);
        messagingTemplate.convertAndSend("/topic/rooms." + room.getId(), messageDto);

        return messageDto;
    }

    @Override
    public Page<ChatMessageDto> getRoomMessages(UUID roomId, Pageable pageable) {
        return messageRepository.findByRoomIdOrderByTimestampDesc(roomId, pageable)
                .map(chatMapper::toDto);
    }

    @Override
    public List<ChatRoomDto> getUserRooms(UUID userId) {
        List<ChatRoom> rooms = roomRepository.findUserRooms(userId);
        return rooms.stream()
                .map(room -> chatMapper.toDto(room, userId))
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoomDto getOrCreateChatRoom(UUID user1Id, UUID user2Id) {
        ChatRoom room = roomRepository.findByUser1IdAndUser2Id(user1Id, user2Id)
                .orElseGet(() -> {
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setUser1(userRepository.getReferenceById(user1Id));
                    newRoom.setUser2(userRepository.getReferenceById(user2Id));
                    return roomRepository.save(newRoom);
                });
        return chatMapper.toDto(room, user1Id);
    }

    private void validateUserInRoom(ChatRoom room, UUID userId) {
        if (!room.getUser1().getId().equals(userId) &&
                !room.getUser2().getId().equals(userId)) {
            throw new RuntimeException("User not authorized");
        }
    }
}