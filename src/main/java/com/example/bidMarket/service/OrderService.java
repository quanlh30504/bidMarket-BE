package com.example.bidMarket.service;

import com.example.bidMarket.Enum.OrderStatus;
import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.model.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    public Order createOrder(OrderDto orderDto);
    public void deleteOrder(UUID orderId);
    public OrderDto getOrder(UUID orderId);
    public List<OrderDto> getAllOrders();
    public void updateStatus (UUID orderId, OrderStatus orderStatus);

    public Page<Order> getOrdersByUserId(UUID userId, int page, int size, String sortBy, String sortDirection);

    public OrderDto getOrderByAuctionId(UUID auctionId);
}
