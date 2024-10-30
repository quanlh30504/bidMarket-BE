package com.example.bidMarket.controller;

import com.example.bidMarket.dto.ChatMessageDto;
import com.example.bidMarket.dto.ChatRoomDto;
import com.example.bidMarket.dto.Request.ChatMessageRequest;
import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.Enum.MessageType;
import com.example.bidMarket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDto sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        return chatService.sendMessage(request, principal.getName());
    }

    @GetMapping("/api/chat/rooms/{userId}")
    @ResponseBody
    public List<ChatRoomDto> getUserRooms(@PathVariable UUID userId) {
        return chatService.getUserRooms(userId);
    }

    @GetMapping("/api/chat/messages/{roomId}")
    @ResponseBody
    public Page<ChatMessageDto> getRoomMessages(
            @PathVariable UUID roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return chatService.getRoomMessages(roomId, PageRequest.of(page, size));
    }

    @PostMapping("/api/chat/rooms")
    @ResponseBody
    public ChatRoomDto createOrGetChatRoom(
            @RequestParam UUID user1Id,
            @RequestParam UUID user2Id) {
        return chatService.getOrCreateChatRoom(user1Id, user2Id);
    }
}
