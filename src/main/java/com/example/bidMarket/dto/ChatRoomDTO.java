package com.example.bidMarket.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatRoomDTO {
    private UUID id;
    private UserDto user1;
    private UserDto user2;
    private MessageDTO lastMessage;
}