package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.model.Order;
import com.example.bidMarket.model.Payment;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.OrderRepository;
import com.example.bidMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class PaymentMapper {
    public final OrderRepository orderRepository;
    public final UserRepository userRepository;
    public static PaymentDto paymentToPaymentDto(Payment payment) {
        List<ProductImage> productImageList = payment.getOrder().getAuction().getProduct().getProductImages();

        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .userId(payment.getUser().getId())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .productImageUrl(!productImageList.isEmpty() ? productImageList.get(0).getImageUrl() : null)
                .build();
    }

    public Payment paymentDtoToPayment(PaymentDto paymentDto){
        Order order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        User user = userRepository.findById(paymentDto.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return Payment.builder()
                .order(order)
                .user(user)
                .transactionId(paymentDto.getTransactionId())
                .amount(paymentDto.getAmount())
                .paymentMethod(paymentDto.getPaymentMethod())
                .status(paymentDto.getStatus())
                .paymentDate(paymentDto.getPaymentDate())
                .build();
    }
}
