package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.ShippingStatus;
import com.example.bidMarket.dto.Request.ShippingCreateRequest;
import com.example.bidMarket.dto.ShippingDto;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.ShippingMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Shipping;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.AuctionRepository;
import com.example.bidMarket.repository.ShippingRepository;
import com.example.bidMarket.repository.UserRepository;
import com.example.bidMarket.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    private final ShippingRepository shippingRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ShippingMapper shippingMapper;

    public String generateTrackingNumber() {
        // Định nghĩa prefix cố định
        String prefix = "SHIP";

        // Lấy timestamp hiện tại và định dạng nó
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);

        // Tạo chuỗi random 6 chữ số
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000); // Đảm bảo 6 chữ số

        // Kết hợp thành tracking number
        return String.format("%s-%s-%06d", prefix, timestamp, randomCode);
    }

    @Override
    public ShippingDto createShipping(ShippingCreateRequest request) {
        Auction auction = auctionRepository.findById(request.getAuctionId()).orElseThrow(()->new AppException(ErrorCode.AUCTION_NOT_FOUND));
        User seller = userRepository.findById(request.getSellerId()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        User buyer = userRepository.findById(request.getBuyerId()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        String trackingNumber = generateTrackingNumber();
        Shipping shipping = new Shipping();
        shipping.setAuction(auction);
        shipping.setSeller(seller);
        shipping.setBuyer(buyer);
        shipping.setPrice(request.getPrice());
        shipping.setStatus(ShippingStatus.PENDING);
        shipping.setTrackingNumber(trackingNumber);
        return shippingMapper.shippingToShippingDto(shippingRepository.save(shipping));
    }

    @Override
    public Page<Shipping> getShippingByBuyer(UUID buyerId, int page, int size, String sortField, String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return shippingRepository.findByBuyerId(buyerId, pageable);
    }

    @Override
    public Page<Shipping> getShippingBuySeller(UUID sellerId, int page, int size, String sortField, String sortDirection) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return shippingRepository.findBySellerId(sellerId, pageable);
    }
}
