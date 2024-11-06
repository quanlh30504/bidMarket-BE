package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.CommentDto;
import com.example.bidMarket.model.Comment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMapper {
    public static CommentDto commentToCommentDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .auctionId(comment.getAuction().getId())
                .content(comment.getContent())
                .createAt(comment.getCreatedAt())
                .updateAt(comment.getUpdatedAt())
                .build();
    }
}
