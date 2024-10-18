package com.example.bidMarket.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {

}
