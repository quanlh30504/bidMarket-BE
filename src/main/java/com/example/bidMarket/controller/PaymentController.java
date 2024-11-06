package com.example.bidMarket.controller;

import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.dto.Response.OrderResponse;
import com.example.bidMarket.mapper.OrderMapper;
import com.example.bidMarket.mapper.PaymentMapper;
import com.example.bidMarket.model.Order;
import com.example.bidMarket.model.Payment;
import com.example.bidMarket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

//    @GetMapping("/user/{userId}")
//    public PaginatedResponse<PaymentDto> getOrdersByUserId(
//            @PathVariable UUID userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "DESC") String sortDirection) {
//        Page<Payment> payments = paymentService.getPaymentsByUserId(userId, page, size, sortBy, sortDirection);
//        List<PaymentDto> content = payments.getContent().stream()
//                .map(PaymentMapper::paymentToPaymentDto)
//                .toList();
//        return new PaginatedResponse<>(
//                payments.getNumber(),
//                payments.getSize(),
//                payments.getTotalElements(),
//                payments.getTotalPages(),
//                payments.isLast(),
//                payments.isFirst(),
//                content
//        );
//    }

}
