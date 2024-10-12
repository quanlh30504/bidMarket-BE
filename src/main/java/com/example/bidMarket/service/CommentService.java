package com.example.bidMarket.service;

import com.example.bidMarket.dto.Request.CreateCommentRequest;
import com.example.bidMarket.dto.Request.UpdateCommentRequest;
import com.example.bidMarket.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment addComment(UUID userId, UUID auctionId, CreateCommentRequest request) throws Exception;
    List<Comment> getCommentsByAuction(UUID auctionId) throws Exception;
    List<Comment> getCommentsByUser(UUID userId) throws Exception;
    Comment updateComment(UUID commentId, UpdateCommentRequest request) throws Exception;
    void deleteComment(UUID commentId) throws Exception;
}