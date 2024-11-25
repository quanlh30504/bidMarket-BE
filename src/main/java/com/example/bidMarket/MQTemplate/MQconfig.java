package com.example.bidMarket.MQTemplate;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQconfig {
    @Autowired
    private BidConsumerMQ bidConsumerMQ;

    @Autowired
    private EmailConsumerMQ emailConsumerMQ;
    @PostConstruct
    private void startMessageQueueTemplate(){
        new Thread(bidConsumerMQ).start();
        new Thread(emailConsumerMQ).start();
    }

}
