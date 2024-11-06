package com.example.bidMarket.controller;

import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.mapper.OrderMapper;
import com.example.bidMarket.model.Order;
import com.example.bidMarket.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        log.info("Start create order");
        return ResponseEntity.ok(orderMapper.orderToOrderDto(orderService.createOrder(orderDto)));
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<OrderDto> getOrderByAuctionId(@PathVariable("auctionId") UUID auctionId) {
        OrderDto orderDto = orderService.getOrderByAuctionId(auctionId);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Order>> getOrdersByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Page<Order> orders = orderService.getOrdersByUserId(userId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(orders);
    }
}
