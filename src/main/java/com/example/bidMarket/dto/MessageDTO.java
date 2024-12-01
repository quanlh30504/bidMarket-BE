package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.MessageStatus;
import com.example.bidMarket.Enum.MessageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MessageDTO {
    private UUID id;
    private String content;
    private MessageType type;
    private String fileUrl;
    private UserDto sender;
    private UUID chatRoomId;
    private LocalDateTime timestamp;
    private MessageStatus status;
}
