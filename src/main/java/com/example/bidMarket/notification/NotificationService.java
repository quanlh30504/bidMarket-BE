package com.example.bidMarket.notification;

import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Bid;

import com.example.bidMarket.repository.BidRepository;
import com.example.bidMarket.repository.WatchListRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final BidRepository bidRepository;
    private final WatchListRepository watchListRepository;


    public NotificationDto createNotification(CreateNotificationRequest request) {
        log.info("Creating notification for user: {}", request.getUserId());

        Notification notification = Notification.builder()
                .message(request.getMessage())
                .userId(request.getUserId())
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        NotificationDto notificationDto = NotificationDto.fromEntity(notification);

        sendNotificationToUser(request.getUserId(), notificationDto);

        return notificationDto;
    }

    public Page<NotificationDto> getUserNotifications(UUID userId, int page, int size) {
        log.info("Fetching notifications for user: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return notifications.map(NotificationDto::fromEntity);
    }

    public List<NotificationDto> getUnreadNotifications(UUID userId) {
        log.info("Fetching unread notifications for user: {}", userId);

        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        log.info("Marking notification as read: {}", notificationId);

        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        log.info("Marking all notifications as read for user: {}", userId);

        List<Notification> unreadNotifications =
                notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);

        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    private void sendNotificationToUser(UUID userId, NotificationDto notification) {
        try {
            log.info("Sending notification to user: {}", userId);

            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "queue/notifications",
                    notification
            );

            log.info("Notification sent successfully to user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
        }
    }

    public void sendOutbidNotification(UUID userId, Auction auction, Double newBidAmount) {
        String message = String.format("You've been outbid on %s! New bid amount: $%.2f",
                auction.getProduct().getName(),
                newBidAmount);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("auctionId", auction.getId().toString());
        metadata.put("type", "OUTBID");
        metadata.put("bidAmount", newBidAmount);

        createAndSendNotification(userId, message, metadata);
    }

    private void createAndSendNotification(UUID userId, String message, Map<String, Object> metadata) {
        Notification notification = Notification.builder()
                .message(message)
                .userId(userId)
                .isRead(false)
                .metadata(convertToJson(metadata))
                .build();

        notification = notificationRepository.save(notification);
        NotificationDto notificationDto = NotificationDto.fromEntity(notification);

        sendNotificationToUser(userId, notificationDto);
    }

    private String convertToJson(Map<String, Object> metadata) {
        try {
            return new ObjectMapper().writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            log.error("Error converting metadata to JSON", e);
            return "{}";
        }
    }


    public void sendAuctionClosedNotification(Auction auction) {
        List<UUID> bidderIds = bidRepository.findByAuctionId(auction.getId()).stream()
                .map(bid -> bid.getUser().getId())
                .distinct()
                .toList();

        List<UUID> watchlistUserIds = watchListRepository.findByAuctionId(auction.getId()).stream()
                .map(watchList -> watchList.getUser().getId())
                .toList();

        Optional<Bid> winningBid = bidRepository.findFirstByAuctionIdOrderByBidAmountDesc(auction.getId());
        UUID winnerId = winningBid.map(bid -> bid.getUser().getId()).orElse(null);

        Set<UUID> allUsersToNotify = new HashSet<>();
        allUsersToNotify.addAll(bidderIds);
        allUsersToNotify.addAll(watchlistUserIds);

        for (UUID userId : allUsersToNotify) {
            String message;
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("auctionId", auction.getId().toString());
            metadata.put("type", "AUCTION_CLOSED");

            if (winnerId != null && userId.equals(winnerId)) {
                message = String.format("Congratulations! You've won the auction for %s with a bid of $%.2f",
                        auction.getTitle(),
                        auction.getCurrentPrice());
                metadata.put("won", true);
            } else if (bidderIds.contains(userId)) {
                message = String.format("The auction for %s has ended. Final price: $%.2f",
                        auction.getTitle(),
                        auction.getCurrentPrice());
            } else {
                message = String.format("An auction you're watching has ended: %s. Final price: $%.2f",
                        auction.getTitle(),
                        auction.getCurrentPrice());
            }

            createAndSendNotification(userId, message, metadata);
        }
    }

}