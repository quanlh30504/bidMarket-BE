package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.PaymentStatus;
import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.mapper.PaymentMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Payment;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.PaymentRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.AuctionService;
import com.example.bidMarket.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto paymentDto) throws Exception {
        User user = userRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new Exception("Not existed user id " + paymentDto.getUserId()));
        Auction auction = auctionRepository.findById(paymentDto.getAuctionId())
                .orElseThrow(() -> new Exception("Not exited auction id " + paymentDto.getAuctionId()));

        Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);
        payment.setUser(user);
        payment.setAuction(auction);
        payment.setStatus(PaymentStatus.UNPAID);
        return paymentMapper.paymentToPaymentDto(paymentRepository.save(payment));
    }



}
