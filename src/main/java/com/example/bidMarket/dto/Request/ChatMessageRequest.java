package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageRequest {
    @NotNull
    private UUID roomId;
    private String content;
    @NotNull
    private MessageType type;
    private String fileUrl;
}
