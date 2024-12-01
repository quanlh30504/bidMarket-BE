package com.example.bidMarket.controller;

import com.example.bidMarket.AWS.AmazonS3Service;
import com.example.bidMarket.dto.ChatRoomDTO;
import com.example.bidMarket.dto.MessageDTO;
import com.example.bidMarket.dto.MessageStatusRequest;
import com.example.bidMarket.dto.Request.MessageRequest;
import com.example.bidMarket.dto.UserDto;
import com.example.bidMarket.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final AmazonS3Service s3Service;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getUserRooms(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getUserRooms(userDetails.getUsername()));
    }

    @PostMapping("/get-or-create-room")
    public ResponseEntity<ChatRoomDTO> getOrCreateRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID otherUserId) {
        ChatRoomDTO room = chatService.getOrCreateRoom(userDetails.getUsername(), otherUserId.toString());
        return ResponseEntity.ok(room);
    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<Page<MessageDTO>> getRoomMessages(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(chatService.getRoomMessages(
                roomId,
                userDetails.getUsername(),
                page,
                size
        ));
    }

    @PostMapping("/room/{roomId}/message")
    public ResponseEntity<MessageDTO> sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID roomId,
            @Valid @RequestBody MessageRequest message) {
        return ResponseEntity.ok(chatService.sendMessage(
                userDetails.getUsername(),
                roomId,
                message
        ));
    }

    @PostMapping("/room/{roomId}/messages/status")
    public ResponseEntity<Void> updateMessageStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID roomId,
            @RequestBody MessageStatusRequest request) {

        chatService.updateMessageStatus(
                roomId,
                userDetails.getUsername(),
                request.getMessageIds(),
                request.getStatus()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}/messages/before")
    public ResponseEntity<Page<MessageDTO>> getMessagesBefore(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID roomId,
            @RequestParam LocalDateTime timestamp,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(chatService.getMessagesBefore(
                roomId,
                userDetails.getUsername(),
                timestamp,
                page,
                size
        ));
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<String> deleteRoom(
            @PathVariable UUID roomId,
            @AuthenticationPrincipal UserDetails userDetails){
        chatService.deleteRoom(roomId, userDetails.getUsername());
        return ResponseEntity.ok("Chatroom deleted");
    }
}