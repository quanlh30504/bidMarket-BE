package com.example.bidMarket.service.impl;

import com.example.bidMarket.dto.Request.CommentCreateRequest;
import com.example.bidMarket.dto.Request.UpdateCommentRequest;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Comment;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.CommentRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRepository auctionRepository;

    @Override
    public Comment getCommentById(UUID id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment don't existed"));
        return comment;
    }

    @Override
    public Comment addComment(CommentCreateRequest request) throws Exception {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setAuction(auction);
        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getCommentsByAuctionId(UUID auctionId, int size, int page, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return commentRepository.findAllByAuctionId(auctionId, pageable);
    }

    @Override
    public List<Comment> getCommentsByUser(UUID userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return commentRepository.findAllByUser(user);
    }

    @Override
    public Comment updateComment(UUID commentId, UpdateCommentRequest request) throws Exception {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setContent(request.getContent());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        commentRepository.delete(comment);
    }
}