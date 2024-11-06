package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.OrderStatus;
import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.OrderMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Order;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.OrderRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public Order createOrder(OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderDto getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::orderToOrderDto).toList();
    }

    @Override
    @Transactional
    public void updateStatus(UUID orderId, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(orderStatus);
            orderRepository.save(order);
        } else {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

    }

    @Override
    public OrderDto getOrderByAuctionId(UUID auctionId) {
        Order order = orderRepository.findByAuctionId(auctionId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    public Page<Order> getOrdersByUserId(UUID userId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findByUserId(userId, pageable);
    }

}
