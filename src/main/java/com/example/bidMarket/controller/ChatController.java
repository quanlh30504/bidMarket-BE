package com.example.bidMarket.controller;

import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.Enum.MessageType;
import com.example.bidMarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        chatService.saveAndSendTextMessage(chatMessage);
    }

    @PostMapping("api/messages/file")
    @ResponseBody
    public ChatMessage sendFileMessage(@RequestParam("file") MultipartFile file,
                                       @RequestParam("senderId") UUID senderId,
                                       @RequestParam("receiverId") UUID receiverId,
                                       @RequestParam("type")MessageType type) throws IOException {
        return chatService.saveAndSendFileMessage(senderId, receiverId, file, type);
    }

    @GetMapping("/api/messages/{user1Id}/{user2Id}")
    public List<ChatMessage> getConversationHistory(@PathVariable UUID user1Id,
                                                    @PathVariable UUID user2Id) {
        return chatService.getConversationHistory(user1Id, user2Id);
    }
}
