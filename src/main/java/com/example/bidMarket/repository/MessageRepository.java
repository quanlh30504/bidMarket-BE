package com.example.bidMarket.repository;

import com.example.bidMarket.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("SELECT m FROM Message m " +
            "WHERE m.chatRoom.id = :roomId " +
            "ORDER BY m.timestamp DESC")
    Page<Message> findByRoomId(@Param("roomId") UUID roomId, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.timestamp < :timestamp " +
            "ORDER BY m.timestamp DESC")
    Page<Message> findByRoomIdAndTimestampBefore(
            @Param("roomId") UUID roomId,
            @Param("timestamp") LocalDateTime timestamp,
            Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m " +
            "WHERE m.chatRoom.id = :roomId AND m.timestamp > :since")
    long countNewMessages(@Param("roomId") UUID roomId, @Param("since") LocalDateTime since);

    void deleteByChatRoomId(UUID chatRoomId);

}