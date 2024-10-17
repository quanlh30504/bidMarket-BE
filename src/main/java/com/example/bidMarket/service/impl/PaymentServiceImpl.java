package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.PaymentMethod;
import com.example.bidMarket.Enum.PaymentStatus;
import com.example.bidMarket.VNPay.VNPayService;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final VNPayService vnPayService;

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public Payment createPayment(PaymentDto paymentDto){
        Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);

        payment = paymentRepository.save(payment);
        log.info("Successfully create payment");
        return payment;

    }

    @Override
    @Transactional
    public PaymentDto processPayment(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");

        // Parse the amount, assuming the VNPay API returns the amount in cents
        BigDecimal amount = new BigDecimal(totalPrice).divide(BigDecimal.valueOf(100));

        // Parse the payment date, assuming VNPay uses yyyyMMddHHmmss format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime parsedPaymentDate = LocalDateTime.parse(paymentTime, formatter);

        PaymentDto paymentDto = PaymentDto.builder()
                .orderId(UUID.fromString(orderInfo))
                .amount(amount)
                .transactionId(transactionId)
                .paymentDate(parsedPaymentDate)
                .paymentMethod(PaymentMethod.VNPAY)
                .bankCode(bankCode)
                .status(paymentStatus == 1 ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .build();
        Payment payment = createPayment(paymentDto);
        return paymentMapper.paymentToPaymentDto(payment);
    }



}
