package com.example.bidMarket.service;


import com.example.bidMarket.Enum.PaymentStatus;
import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.model.Payment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface PaymentService {

    public Payment createPayment(PaymentDto paymentDto);
    public PaymentDto processPayment(HttpServletRequest request);

}
