package com.example.bidMarket.dto;

import com.example.bidMarket.Enum.MessageStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MessageStatusRequest {
    private UUID roomId;
    private UUID userId;
    private List<UUID> messageIds;
    private MessageStatus status;
}
