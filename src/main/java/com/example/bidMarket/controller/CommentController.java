package com.example.bidMarket.controller;

import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.CommentEvent;
import com.example.bidMarket.dto.Request.CommentCreateRequest;
import com.example.bidMarket.dto.Request.UpdateCommentRequest;
import com.example.bidMarket.dto.Response.CommentResponse;
import com.example.bidMarket.mapper.CommentMapper;
import com.example.bidMarket.model.Comment;
import com.example.bidMarket.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/comment")
    public ResponseEntity<?> addNewComment(CommentCreateRequest comment) throws Exception {
        try {
            Comment savedComment = commentService.addComment(comment);
            CommentResponse commentResponse = CommentMapper.commentToCommentResponse(savedComment);
            messagingTemplate.convertAndSend("/topic/auction-comments/" + comment.getAuctionId(),
                    new CommentEvent("create", commentResponse));
            return ResponseEntity.ok("Add comment successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/auction/{auctionId}")
    public PaginatedResponse<CommentResponse> getCommentsByAuction(
            @PathVariable UUID auctionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Page<Comment> commentPage = commentService.getCommentsByAuctionId(auctionId, size, page, sortBy, sortDirection);
        List<CommentResponse> content = commentPage.getContent().stream()
                .map(CommentMapper::commentToCommentResponse)
                .toList();
        return new PaginatedResponse<>(
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages(),
                commentPage.isLast(),
                commentPage.isFirst(),
                content
        );
    }

    @PutMapping("/{auctionId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable UUID auctionId, @PathVariable UUID commentId, @RequestBody UpdateCommentRequest request) {
        try {
            Comment newComment = commentService.updateComment(commentId, request);

            messagingTemplate.convertAndSend("/topic/auction-comments/" + auctionId,
                    new CommentEvent("update", CommentMapper.commentToCommentResponse(newComment)));
            return ResponseEntity.ok("Update comment successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{auctionId}/{commentId}")
    public void deleteComment(@PathVariable UUID auctionId, @PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
        // Phát sự kiện "delete" qua WebSocket
        messagingTemplate.convertAndSend("/topic/auction-comments/" + auctionId,
                new CommentEvent("delete", commentId));
    }


}