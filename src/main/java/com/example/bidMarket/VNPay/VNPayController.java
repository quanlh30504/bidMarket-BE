package com.example.bidMarket.VNPay;


import com.example.bidMarket.Enum.OrderStatus;
import com.example.bidMarket.Enum.PaymentMethod;
import com.example.bidMarket.Enum.PaymentStatus;
import com.example.bidMarket.dto.PaymentDto;
import com.example.bidMarket.mapper.PaymentMapper;
import com.example.bidMarket.service.OrderService;
import com.example.bidMarket.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnPayService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    private final PaymentMapper paymentMapper;

    /*
    - Chuyển hướng người dùng đến cổng thanh toán VNPAY
    - order info is order id
    */
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public PaymentDto paymentCompleted(HttpServletRequest request, Model model) {
        PaymentDto paymentDto = paymentService.processPayment(request);

        // Cập nhật trạng thái đơn hàng nếu thanh toán thành công
        if (paymentDto.getStatus() == PaymentStatus.SUCCESS) {
            orderService.updateStatus(paymentDto.getOrderId(), OrderStatus.PAID);
        }

        model.addAttribute("orderId", paymentDto.getOrderId());
        model.addAttribute("totalPrice", paymentDto.getAmount());
        model.addAttribute("paymentTime", paymentDto.getPaymentDate());
        model.addAttribute("transactionId", paymentDto.getTransactionId());

        return paymentDto;
    }
}
