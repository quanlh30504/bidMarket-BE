package com.example.bidMarket.MQTemplate;

import com.example.bidMarket.dto.Request.EmailRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailProvider {
    @Autowired
    public UserRepository userRepository;
    public MessageQueueTemplate<Object> messageQueue;

    public EmailProvider(MessageQueueTemplate<Object> messageQueue) {
        this.messageQueue = messageQueue;
        messageQueue.createTopic("email_OTP_request");
    }

    public void sendEmailOTPRequest(EmailRequest email) {
        messageQueue.sendMessage("email_OTP_request", email);
    }
}
