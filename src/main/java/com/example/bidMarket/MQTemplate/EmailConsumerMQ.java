package com.example.bidMarket.MQTemplate;

import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Request.EmailRequest;
import com.example.bidMarket.service.BidService;
import com.example.bidMarket.service.impl.VerifyEmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
public class EmailConsumerMQ implements Runnable{
    @Autowired
    private BidService bidService;
    @Autowired
    private VerifyEmailServiceImpl verifyEmailService;

    private final MessageQueueTemplate<Object> messageQueue;

    // Map chứa tên các topic và kiểu dữ liệu tương ứng để xử lý
    private final Map<String, Class<?>> topicTypeMapping = new HashMap<>();

    @Autowired
    public EmailConsumerMQ(MessageQueueTemplate<Object> messageQueue) {
        this.messageQueue = messageQueue;

        // Định nghĩa các topic và kiểu dữ liệu của chúng
//        topicTypeMapping.put("bid_request", BidCreateRequest.class);
        topicTypeMapping.put("email_OTP_request", EmailRequest.class);
        // Thêm các topic khác và kiểu dữ liệu của chúng tại đây
    }

    @Override
    public void run() {
        while (true) {
            for (Map.Entry<String, Class<?>> entry : topicTypeMapping.entrySet()) {
                String topic = entry.getKey();
                Class<?> messageType = entry.getValue();

                // Nhận tin nhắn từ topic và chuyển về đúng kiểu dữ liệu
                Object message = messageQueue.receiveMessage(topic);
                if (message != null) {
                    log.info("Start processing message: " + message.getClass());
                    processMessage(message, messageType);
                }
            }

            // Thêm một khoảng thời gian nghỉ để giảm tải cho vòng lặp nếu cần
            try {
                Thread.sleep(100); // thời gian nghỉ 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Async
    private void processMessage(Object message, Class<?> messageType) {
//        if (messageType == BidCreateRequest.class) {
//            bidService.processBid((BidCreateRequest) message);
//        } else if (messageType == EmailRequest.class) {
//            EmailRequest emailRequest = (EmailRequest) message;
//            verifyEmailService.sendOtp(emailRequest.getEmail());
//        }
        EmailRequest emailRequest = (EmailRequest) message;
        verifyEmailService.sendOtp(emailRequest.getEmail());
        // Thêm các trường hợp xử lý khác tương ứng với các topic
    }
}
