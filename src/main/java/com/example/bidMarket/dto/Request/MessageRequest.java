package com.example.bidMarket.dto.Request;

import com.example.bidMarket.Enum.MessageType;
import lombok.Data;

@Data
public class MessageRequest {
    private String content;
    private MessageType type;
    private String fileUrl;
}