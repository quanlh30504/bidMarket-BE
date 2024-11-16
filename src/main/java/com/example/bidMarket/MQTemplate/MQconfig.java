package com.example.bidMarket.MQTemplate;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQconfig {
    @Autowired
    private ConsumerMQ consumerMQ;

    @PostConstruct
    private void startMessageQueueTemplate(){
        new Thread(consumerMQ).start();
    }

}
