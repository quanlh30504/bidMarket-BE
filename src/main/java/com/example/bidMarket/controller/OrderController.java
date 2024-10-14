package com.example.bidMarket.controller;

import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.mapper.OrderMapper;
import com.example.bidMarket.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
