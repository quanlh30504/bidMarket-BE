package com.example.bidMarket.repository;

import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
            UUID senderId1, UUID receiverId1, UUID senderId2, UUID receiverId2);
}
