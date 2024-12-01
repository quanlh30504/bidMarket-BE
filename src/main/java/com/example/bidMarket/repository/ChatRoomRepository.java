package com.example.bidMarket.repository;

import com.example.bidMarket.model.ChatRoom;
import com.example.bidMarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE cr.user1 = :user OR cr.user2 = :user " +
            "ORDER BY (SELECT MAX(m.timestamp) FROM Message m WHERE m.chatRoom = cr) DESC NULLS LAST")
    List<ChatRoom> findByUser1OrUser2(@Param("user") User user);

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.user1 = :user1 AND cr.user2 = :user2) " +
            "OR (cr.user1 = :user2 AND cr.user2 = :user1)")
    Optional<ChatRoom> findByUsers(
            @Param("user1") User user1,
            @Param("user2") User user2);

    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.messages m " +
            "WHERE cr.id = :roomId " +
            "ORDER BY m.timestamp DESC")
    Optional<ChatRoom> findByIdWithMessages(@Param("roomId") UUID roomId);
}