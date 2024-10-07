package com.example.bidMarket.KafkaService;

import com.example.bidMarket.dto.Request.BidCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidProducer {
    private final KafkaTemplate<String, BidCreateRequest> kafkaTemplate;
    @Value("${spring.kafka.topic.bid_request}")
    private String topic_bidRequest;

    public void sendBidRequest(BidCreateRequest bidRequest) {
        // Sử dụng auctionId làm key
        String auctionIdKey = bidRequest.getAuctionId().toString();

        // Tạo ProducerRecord với key là auctionId
        kafkaTemplate.send(topic_bidRequest, auctionIdKey, bidRequest);

        log.info("Message sent to Kafka with auctionId as key: " + auctionIdKey);
    }
}
