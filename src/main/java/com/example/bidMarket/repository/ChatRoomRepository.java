package com.example.bidMarket.repository;

import com.example.bidMarket.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    @Query("SELECT r FROM ChatRoom r WHERE r.user1.id = :userId OR r.user2.id = :userId")
    List<ChatRoom> findUserRooms(@Param("userId") UUID userId);

    @Query("SELECT r FROM ChatRoom r WHERE " +
            "(r.user1.id = :user1Id AND r.user2.id = :user2Id) OR " +
            "(r.user1.id = :user2Id AND r.user2.id = :user1Id)")
    Optional<ChatRoom> findByUser1IdAndUser2Id(
            @Param("user1Id") UUID user1Id,
            @Param("user2Id") UUID user2Id);
}