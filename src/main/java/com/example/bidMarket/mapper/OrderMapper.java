package com.example.bidMarket.mapper;


import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.dto.Response.OrderResponse;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Order;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderMapper {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    public Order orderDtoToOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(orderDto.getAuctionId())
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        return Order.builder()
                .auction(auction)
                .user(user)
                .totalAmount(orderDto.getTotalAmount())
                .status(orderDto.getStatus())
                .paymentDueDate(orderDto.getPaymentDueDate())
                .build();
    }


    public OrderDto orderToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .auctionId(order.getAuction().getId())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentDueDate(order.getPaymentDueDate())
                .build();
    }

    public static OrderResponse orderToOrderResponse(Order order) {
        List<ProductImage> productImageList = order.getAuction().getProduct().getProductImages();
        return OrderResponse.builder()
                .id(order.getId())
                .auctionId(order.getAuction().getId())
                .userId(order.getUser().getId())
                .userEmail(order.getUser().getEmail())
                .auctionTitle(order.getAuction().getTitle())
                .productImageUrl(!productImageList.isEmpty() ? productImageList.get(0).getImageUrl() : null)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentDueDate(order.getPaymentDueDate())
                .createAt(order.getCreatedAt())
                .build();
    }
}
