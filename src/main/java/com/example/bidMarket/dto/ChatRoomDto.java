package com.example.bidMarket.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ChatRoomDto {
    private UUID id;
    private UserDto otherUser;
    private ChatMessageDto lastMessage;
}