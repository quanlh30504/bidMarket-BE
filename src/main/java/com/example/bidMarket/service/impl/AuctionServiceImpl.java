package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.*;
import com.example.bidMarket.SearchService.AuctionSpecification;
import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.Auction;
import com.example.bidMarket.model.Bid;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.model.User;
import com.example.bidMarket.repository.*;
import com.example.bidMarket.service.AuctionService;
import com.example.bidMarket.service.OrderService;
import com.example.bidMarket.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;

    private final AuctionMapper auctionMapper;

    private final ProductService productService;
    private final OrderService orderService;

    @Override
    public List<AuctionDto> getAllAuction() {
        List<Auction> auctions = auctionRepository.findAll();
        List<AuctionDto> auctionDtos = auctions.stream().map(auctionMapper::auctionToAuctionDto)
                .toList();
        return auctionDtos;
    }

    @Override
    public AuctionDto getAuctionById(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        return auctionMapper.auctionToAuctionDto(auction);
    }

    @Override
    public Page<Auction> searchAuctions(String title,
                                        CategoryType categoryType,
                                        AuctionStatus status,
                                        BigDecimal minPrice, BigDecimal maxPrice,
                                        LocalDateTime startTime, LocalDateTime endTime,
                                        int page, int size, String sortField, Sort.Direction sortDirection) {
        Specification<Auction> spec = Specification
                .where(AuctionSpecification.hasTitle(title))
                .and(AuctionSpecification.hasCategoryType(categoryType))
                .and(AuctionSpecification.hasStatus(status))
                .and(AuctionSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(AuctionSpecification.hasStartTimeBetween(startTime, endTime));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        Page<Auction>auctions = auctionRepository.findAll(spec, pageable);
        return auctions;

    }

    @Override
    @Transactional
    public AuctionDto createAuction(AuctionCreateRequest auctionCreateRequest) throws Exception {
        // Tạo Product entity từ DTO
        Product product = productService.createProduct(auctionCreateRequest.getProductCreateRequest());

        // Tạo phiên đấu giá từ DTO và product đã được lưu
        Auction auction = auctionMapper.auctionCreateToAuction(auctionCreateRequest, product);
        auction = auctionRepository.save(auction);

        AuctionDto auctionDto = auctionMapper.auctionToAuctionDto(auction);
        return auctionDto;
    }

    @Override
    @Transactional
    public AuctionDto changeAuctionStatus(UUID id, AuctionStatus status) throws Exception {
        Auction auction = auctionRepository.findById(id).orElseThrow(() -> new Exception("Not found auction id"));
        if (auction.getStatus() == AuctionStatus.COMPLETED || auction.getStatus() == AuctionStatus.CLOSED) {
            throw new Exception("Auction was completed or canceled, so can't change status");
        }
        auction.setStatus(status);
        return auctionMapper.auctionToAuctionDto(auctionRepository.save(auction));
    }

    @Override
    @Transactional
    public Auction updateAuction(UUID id, AuctionUpdateRequest request) {
        log.info("Start update auction");
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() != AuctionStatus.PENDING && auction.getStatus() != AuctionStatus.CANCELED){
            throw new AppException(ErrorCode.AUCTION_UPDATE_FAILED);
        }
        if (request.getStartTime().isAfter(request.getEndTime())){
            throw new AppException(ErrorCode.AUCTION_UPDATE_FAILED);
        }
        auction.setTitle(request.getTitle());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setCurrentPrice(request.getStartingPrice());
        auction.setMinimumBidIncrement(request.getMinimumBidIncrement());

        productService.updateProduct(auction.getProduct().getId(), request.getProductUpdateRequest());

        auction = auctionRepository.save(auction);
        return auction;
    }

    @Override
    @Transactional
    public void deleteAuction(UUID id) {
        log.info("Start delete auction");
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (    auction.getStatus() == AuctionStatus.OPEN ||
                auction.getStatus() == AuctionStatus.EXTENDED ||
                auction.getStatus() == AuctionStatus.CLOSED) {
            throw new AppException(ErrorCode.AUCTION_DELETE_FAILED);
        }
        Product product = auction.getProduct();
        if (auction.getStatus() == AuctionStatus.PENDING) {
            product.setStatus(ProductStatus.INACTIVE);
            productRepository.save(product);
        }else if (auction.getStatus() == AuctionStatus.CANCELED || auction.getStatus() == AuctionStatus.COMPLETED) {
            productRepository.delete(product);
        }
        auctionRepository.delete(auction);
    }

    @Override
    @Transactional
    public void openAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        if (auction.getStatus() != AuctionStatus.PENDING) {
            throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
        }
//
//        if (auction.getStartTime().isAfter(LocalDateTime.now())) {
//            throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
//        }

        Product product = auction.getProduct();
        if (product == null || product.getStatus() != ProductStatus.INACTIVE) {
            throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
        }

        auction.setStatus(AuctionStatus.OPEN);
        product.setStatus(ProductStatus.ACTIVE);

        auctionRepository.save(auction);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void closeAuction(UUID auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.OPEN){
            auction.setStatus(AuctionStatus.CLOSED);
            auctionRepository.save(auction);

            Optional <Bid> winBid = bidRepository.findFirstByAuctionIdAndStatusOrderByBidAmountDesc(auction.getId(), BidStatus.VALID);
            if (winBid.isEmpty()){
                throw new AppException(ErrorCode.AUCTION_NOT_HAVE_BID);
            }

            Bid bid = winBid.get();
            OrderDto orderDto = OrderDto.builder()
                    .userId(bid.getUserId())
                    .auctionId(auction.getId())
                    .totalAmount(bid.getBidAmount())
                    .paymentDueDate(LocalDateTime.now().plusDays(5)) // Hard code: payment due date is 5 days from created order date.
                    .status(OrderStatus.PENDING)
                    .build();
            // Create order
            orderService.createOrder(orderDto);
        }else {
            throw new AppException(ErrorCode.AUCTION_CLOSE_FAILED);
        }
    }

    @Override
    public void cancelAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() != AuctionStatus.COMPLETED){
            auction.setStatus(AuctionStatus.CANCELED);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.INACTIVE);
            auctionRepository.save(auction);
            productRepository.save(product);
        }else {
            throw new AppException(ErrorCode.AUCTION_CANCEL_FAILED);
        }
    }

    @Override
    public void completeAuction(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.CLOSED){
            auction.setStatus(AuctionStatus.COMPLETED);
            Product product = auction.getProduct();
            product.setStatus(ProductStatus.SOLD);
            auctionRepository.save(auction);
            productRepository.save(product);
        }else {
            throw new AppException(ErrorCode.AUCTION_COMPLETE_FAILED);
        }
    }

    @Override
    public void reOpenAuction(UUID id, AuctionUpdateRequest request) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getStatus() == AuctionStatus.CANCELED){
            auction = updateAuction(id, request);
            auction.setStatus(AuctionStatus.PENDING);
            auctionRepository.save(auction);
        }else {
            throw new AppException(ErrorCode.AUCTION_COMPLETE_FAILED);
        }
    }




}
