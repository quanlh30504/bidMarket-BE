package com.example.bidMarket.MQTemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class MessageQueueTemplate<T> {
    private final Map<String, BlockingQueue<T>> topicQueues = new ConcurrentHashMap<>();

    public void createTopic(String topicName) {
        topicQueues.putIfAbsent(topicName, new LinkedBlockingQueue<>());
    }

    public void sendMessage(String topicName, T message) {
        BlockingQueue<T> queue = topicQueues.get(topicName);
        if (queue != null) {
            try {
                queue.put(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Failed to send message to topic " + topicName, e);
            }
        } else {
            throw new IllegalArgumentException("Topic " + topicName + " does not exist");
        }
    }

    public T receiveMessage(String topicName) {
        BlockingQueue<T> queue = topicQueues.get(topicName);
        if (queue != null) {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Failed to receive message from topic " + topicName, e);
            }
        } else {
            throw new IllegalArgumentException("Topic " + topicName + " does not exist");
        }
    }
}
