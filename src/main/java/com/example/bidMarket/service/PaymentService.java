package com.example.bidMarket.service;


import com.example.bidMarket.Enum.PaymentStatus;
import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.model.Payment;

import java.util.UUID;

public interface PaymentService {

    public PaymentDto createPayment(PaymentDto paymentDto) throws Exception;

}
