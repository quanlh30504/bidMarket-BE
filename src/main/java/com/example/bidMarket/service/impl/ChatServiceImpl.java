package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.MessageStatus;
import com.example.bidMarket.dto.ChatRoomDTO;
import com.example.bidMarket.dto.MessageDTO;
import com.example.bidMarket.dto.Request.MessageRequest;
import com.example.bidMarket.mapper.UserMapper;
import com.example.bidMarket.model.ChatRoom;
import com.example.bidMarket.model.Message;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.ChatRoomRepository;
import com.example.bidMarket.repository.MessageRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.ChatService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserMapper userMapper;

    @Value("${app.chat.max-page-size:50}")
    private int maxPageSize;

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDTO> getUserRooms(String userId) {
        User user = getUserById(userId);
        List<ChatRoom> rooms = roomRepository.findByUser1OrUser2(user);
        return rooms.stream()
                .map(this::convertToRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatRoomDTO getOrCreateRoom(String userId, String otherUserId) {
        User user1 = getUserById(userId);
        User user2 = getUserById(otherUserId);

        if (user1.getId().equals(user2.getId())) {
            throw new IllegalArgumentException("You cannot create a chat room with yourself.");
        }

        ChatRoom room = roomRepository.findByUsers(user1, user2)
                .orElseGet(() -> {
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setUser1(user1);
                    newRoom.setUser2(user2);
                    return roomRepository.save(newRoom);
                });

        return convertToRoomDTO(room);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getRoomMessages(UUID roomId, String userId, int page, int size) {
        size = Math.min(size, maxPageSize);

        User user = getUserById(userId);

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!room.getUser1().getId().equals(user.getId()) &&
                !room.getUser2().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have access to this chat room");
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Message> messages = messageRepository.findByRoomId(roomId, pageRequest);

        return messages.map(this::convertToMessageDTO);
    }

    @Override
    @Transactional
    public MessageDTO sendMessage(String userId, UUID roomId, MessageRequest messageRequest) {
        if (StringUtils.isBlank(messageRequest.getContent()) &&
                StringUtils.isBlank(messageRequest.getFileUrl())) {
            throw new IllegalArgumentException("Message content must be provided");
        }

        User sender = getUserById(userId);

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!room.getUser1().getId().equals(sender.getId()) &&
                !room.getUser2().getId().equals(sender.getId())) {
            throw new AccessDeniedException("You don't have access to this chat room");
        }

        Message message = new Message();
        message.setChatRoom(room);
        message.setSender(sender);
        message.setContent(messageRequest.getContent());
        message.setType(messageRequest.getType());
        message.setFileUrl(messageRequest.getFileUrl());

        Message savedMessage = messageRepository.save(message);
        MessageDTO messageDTO = convertToMessageDTO(savedMessage);

        log.info("Sending WebSocket message to room: {}", roomId);
        messagingTemplate.convertAndSend("/topic/room." + roomId, messageDTO);

        User otherUser = room.getUser1().getId().equals(sender.getId()) ? room.getUser2() : room.getUser1();
        log.info("Sending private message to user: {}", otherUser.getId());
        messagingTemplate.convertAndSendToUser(
                otherUser.getId().toString(),
                "/queue/messages",
                messageDTO
        );

        room.getMessages().add(0, savedMessage);
        roomRepository.save(room);
        log.info("Message sent successfully. MessageId: {}", messageDTO.getId());

        return messageDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessagesBefore(UUID roomId, String email,
                                              LocalDateTime timestamp, int page, int size) {
        size = Math.min(size, maxPageSize);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!room.getUser1().getId().equals(user.getId()) &&
                !room.getUser2().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have access to this chat room");
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Message> messages = messageRepository.findByRoomIdAndTimestampBefore(
                roomId, timestamp, pageRequest);

        return messages.map(this::convertToMessageDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getNewMessageCount(UUID roomId, String email, LocalDateTime since) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!room.getUser1().getId().equals(user.getId()) &&
                !room.getUser2().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have access to this chat room");
        }

        return messageRepository.countNewMessages(roomId, since);
    }

    private ChatRoomDTO convertToRoomDTO(ChatRoom room) {
        return ChatRoomDTO.builder()
                .id(room.getId())
                .user1(userMapper.userToUserDto(room.getUser1()))
                .user2(userMapper.userToUserDto(room.getUser2()))
                .lastMessage(room.getMessages().isEmpty() ? null :
                        convertToMessageDTO(room.getMessages().get(0)))
                .build();
    }

    private MessageDTO convertToMessageDTO(Message message) {
        try {
            MessageDTO dto = MessageDTO.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .type(message.getType())
                    .fileUrl(message.getFileUrl())
                    .sender(userMapper.userToUserDto(message.getSender()))
                    .timestamp(message.getTimestamp())
                    .chatRoomId(message.getChatRoom().getId())
                    .build();
            log.debug("Converted message to DTO: {}", dto);
            return dto;
        } catch (Exception e) {
            log.error("Error converting message to DTO. MessageId: {}", message.getId(), e);
            throw e;
        }
    }

    @Override
    public void updateMessageStatus(UUID roomId, String userId, List<UUID> messageIds, MessageStatus status) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        verifyRoomAccess(room, user);

        List<Message> messages = messageRepository.findAllById(messageIds);

        messages.stream()
                .filter(message -> !message.getSender().getId().equals(user.getId()))
                .forEach(message -> {
                    message.setStatus(status);
                    messageRepository.save(message);
                });
    }

    public User getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    public void verifyRoomAccess(ChatRoom room, User user) {
        if (!room.getUser1().getId().equals(user.getId()) &&
                !room.getUser2().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have access to this chat room");
        }
    }

    @Override
    @Transactional
    public void deleteRoom(UUID roomId, String userId) {
        User user = getUserById(userId);
        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        verifyRoomAccess(room, user);

        messageRepository.deleteByChatRoomId(roomId);

        roomRepository.delete(room);

        log.info("Conversation with roomId {} deleted by user {}", roomId, userId);
    }

}