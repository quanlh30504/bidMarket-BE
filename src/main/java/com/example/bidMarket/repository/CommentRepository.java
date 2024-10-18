package com.example.bidMarket.repository;

import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Comment;
import com.example.bidMarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByAuction(Auction auction);
    List<Comment> findAllByUser(User user);
}
