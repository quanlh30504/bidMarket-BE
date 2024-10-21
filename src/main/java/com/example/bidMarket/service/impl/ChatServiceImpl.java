package com.example.bidMarket.service.impl;

import com.example.bidMarket.AWS.AmazonS3Service;
import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.Enum.MessageType;
import com.example.bidMarket.repository.ChatMessageRepository;
import com.example.bidMarket.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Override
    public ChatMessage saveAndSendTextMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        message.setType(MessageType.TEXT);
        ChatMessage savedMessage = chatMessageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/messages" + message.getReceiverId(), savedMessage);

        return savedMessage;
    }

    @Override
    public ChatMessage saveAndSendFileMessage(UUID senderId, UUID receiverId, MultipartFile file, MessageType type) throws IOException {
        String folder = "chat/" + UUID.randomUUID().toString();
        String fileUrl = amazonS3Service.uploadFile(file, folder);

        ChatMessage message = new ChatMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        //???
        message.setContent(file.getOriginalFilename());
        message.setFileUrl(fileUrl);
        message.setType(type);
        message.setTimestamp(LocalDateTime.now());

        ChatMessage savedMessage = chatMessageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/messages" + receiverId, savedMessage);
        return savedMessage;
    }


    @Override
    public List<ChatMessage> getConversationHistory(UUID user1Id, UUID user2Id) {
        return chatMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                user1Id, user2Id, user2Id, user1Id);
    }
}
