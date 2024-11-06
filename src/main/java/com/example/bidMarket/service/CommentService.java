package com.example.bidMarket.service;

import com.example.bidMarket.dto.Request.CreateCommentRequest;
import com.example.bidMarket.dto.Request.UpdateCommentRequest;
import com.example.bidMarket.model.Comment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment addComment(UUID userId, UUID auctionId, CreateCommentRequest request) throws Exception;
    public Page<Comment> getCommentsByAuctionId(UUID auctionId, int size, int page, String sortBy, String sortDirection);
    List<Comment> getCommentsByUser(UUID userId) throws Exception;
    Comment updateComment(UUID commentId, UpdateCommentRequest request) throws Exception;
    void deleteComment(UUID commentId) throws Exception;
}