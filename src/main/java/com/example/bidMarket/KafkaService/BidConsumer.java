//package com.example.bidMarket.KafkaService;
//
//import com.example.bidMarket.dto.Request.BidCreateRequest;
//import com.example.bidMarket.service.BidService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class BidConsumer {
//
//    @Autowired
//    private BidService bidService;
//
//    @KafkaListener(topics = "${spring.kafka.topic.bid_request}", groupId = "${spring.kafka.consumer.group-id}")
//    public void listenBidRequest(BidCreateRequest bidRequest){
//        log.info("processing bid in auction id " + bidRequest.getAuctionId());
//        bidService.processBid(bidRequest);
//
//    }
//}
