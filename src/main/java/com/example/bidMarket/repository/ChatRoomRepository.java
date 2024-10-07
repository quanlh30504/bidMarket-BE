package com.example.bidMarket.repository;

import com.example.bidMarket.model.ChatRoom;
import com.example.bidMarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    List<ChatRoom> findByParticipantOneOrParticipantTwo(User participantOne, User participantTwo);
}
