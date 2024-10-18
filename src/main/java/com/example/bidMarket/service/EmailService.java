package com.example.bidMarket.service;

import com.example.bidMarket.dto.MailBody;

public interface EmailService {
    void sendSimpleMessage(MailBody mailBody);
}
