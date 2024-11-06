package com.example.bidMarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentEvent {
    private String action;
    private Object data;
}