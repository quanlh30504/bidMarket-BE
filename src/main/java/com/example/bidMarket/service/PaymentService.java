package com.example.bidMarket.service;

import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.model.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PaymentService {

    public Payment createPayment(PaymentDto paymentDto);
    public PaymentDto processPayment(HttpServletRequest request);

//    public Page<Payment> getPaymentsByUserId(UUID userId, int page, int size, String sortBy, String sortDirection);
}
