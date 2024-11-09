package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.MessageStatus;
import com.example.bidMarket.Enum.MessageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ChatMessageDto {
    private UUID id;
    private UUID roomId;
    private UUID senderId;
    private String senderName;
    private String content;
    private String fileUrl;
    private MessageType type;
    private LocalDateTime timestamp;
}
