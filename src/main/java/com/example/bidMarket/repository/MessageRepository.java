package com.example.bidMarket.repository;

import com.example.bidMarket.model.ChatRoom;
import com.example.bidMarket.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatRoomOrderBySentAtDesc(ChatRoom chatRoom);
}
