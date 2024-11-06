package com.example.bidMarket.dto.Response;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    UUID id;
    UUID auctionId;
    UUID userId;
    String userEmail;
    String userAvatarUrl;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
