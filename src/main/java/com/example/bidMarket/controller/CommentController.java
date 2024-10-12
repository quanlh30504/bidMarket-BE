package com.example.bidMarket.controller;

import com.example.bidMarket.dto.Request.CreateCommentRequest;
import com.example.bidMarket.dto.Request.UpdateCommentRequest;
import com.example.bidMarket.model.Comment;
import com.example.bidMarket.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/auction/{auctionId}/user/{userId}")
    public ResponseEntity<Comment> addComment(@PathVariable UUID userId, @PathVariable UUID auctionId, @RequestBody CreateCommentRequest request) {
        try {
            Comment comment = commentService.addComment(userId, auctionId, request);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<Comment>> getCommentsByAuction(@PathVariable UUID auctionId) {
        try {
            List<Comment> comments = commentService.getCommentsByAuction(auctionId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable UUID userId) {
        try {
            List<Comment> comments = commentService.getCommentsByUser(userId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable UUID commentId, @RequestBody UpdateCommentRequest request) {
        try {
            Comment comment = commentService.updateComment(commentId, request);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}