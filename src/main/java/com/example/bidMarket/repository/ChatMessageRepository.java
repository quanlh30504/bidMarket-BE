package com.example.bidMarket.repository;

import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findByRoomIdOrderByTimestampDesc(UUID roomId, Pageable pageable);
}