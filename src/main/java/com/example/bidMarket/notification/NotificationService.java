package com.example.bidMarket.notification;

import com.example.bidMarket.Enum.BidStatus;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Bid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

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

        Notification notification = Notification.builder()
                .message(message)
                .userId(userId)
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        NotificationDto notificationDto = NotificationDto.fromEntity(notification);

        sendNotificationToUser(userId, notificationDto);
    }

    private void createAndSendNotification(UUID userId, String message) {
        Notification notification = Notification.builder()
                .message(message)
                .userId(userId)
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        NotificationDto notificationDto = NotificationDto.fromEntity(notification);

        sendNotificationToUser(userId, notificationDto);
    }

}