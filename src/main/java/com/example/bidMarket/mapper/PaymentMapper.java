package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Order;
import com.example.bidMarket.model.Payment;
import com.example.bidMarket.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PaymentMapper {
    public final OrderRepository orderRepository;
    public static PaymentDto paymentToPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    public Payment paymentDtoToPayment(PaymentDto paymentDto){
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return Payment.builder()
                .order(order)
                .transactionId(paymentDto.getTransactionId())
                .amount(paymentDto.getAmount())
                .paymentMethod(paymentDto.getPaymentMethod())
                .status(paymentDto.getStatus())
                .paymentDate(paymentDto.getPaymentDate())
                .build();
    }
}
