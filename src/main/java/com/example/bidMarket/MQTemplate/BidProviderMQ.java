package com.example.bidMarket.MQTemplate;

import com.example.bidMarket.Enum.AuctionStatus;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.repository.AuctionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BidProviderMQ {
    private MessageQueueTemplate<Object> messageQueue;

    public BidProviderMQ(MessageQueueTemplate<Object> messageQueue) {
        this.messageQueue = messageQueue;
        messageQueue.createTopic("bid_request");
    }

    @Autowired
    private AuctionRepository auctionRepository;

    public void sendBidRequest(BidCreateRequest bidRequest) {

        Auction auction = auctionRepository.findById(bidRequest.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() != AuctionStatus.OPEN) {
            throw new AppException(ErrorCode.BID_IS_REJECTED);
        } else {
            // Tạo ProducerRecord với key là auctionId
            messageQueue.sendMessage("bid_request", bidRequest);

            log.info("Message sent to MQ with auctionId as key: " + auction.getId());
        }
    }

}
