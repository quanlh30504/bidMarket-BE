package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.*;
import com.example.bidMarket.SearchService.AuctionSpecification;
import com.example.bidMarket.dto.OrderDto;
import com.example.bidMarket.dto.Request.AuctionCreateRequest;
import com.example.bidMarket.dto.AuctionDto;
import com.example.bidMarket.dto.Request.AuctionUpdateRequest;
import com.example.bidMarket.dto.Response.AuctionSearchResponse;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.AuctionMapper;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.*;
import com.example.bidMarket.repository.*;
import com.example.bidMarket.service.AuctionService;
import com.example.bidMarket.service.BidService;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class AuctionServiceImpl implements AuctionService {
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;

    private final AuctionMapper auctionMapper;

    private final ProductService productService;
    private final OrderService orderService;
    private final BidService bidService;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<AuctionDto> getAllAuction() {
        List<Auction> auctions = auctionRepository.findAll();
        List<AuctionDto> auctionDtos = auctions.stream().map(auctionMapper::auctionToAuctionDto)
                .toList();
        return auctionDtos;
    }

    @Override
    public AuctionSearchResponse getAuctionById(UUID id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));
        return auctionMapper.auctionToAuctionSearchResponse(auction);
    }

    @Override
    public Page<Auction> searchAuctions(UUID sellerId,
                                        String title,
                                        List<CategoryType> categoryType,
                                        AuctionStatus status,
                                        BigDecimal minPrice, BigDecimal maxPrice,
                                        LocalDateTime startTime, LocalDateTime endTime,
                                        int page, int size, String sortField, Sort.Direction sortDirection) {
        Specification<Auction> spec = Specification
                .where(AuctionSpecification.hasSellerId(sellerId))
                .and(AuctionSpecification.hasTitle(title))
                .and(AuctionSpecification.hasCategoryTypes(categoryType))
                .and(AuctionSpecification.hasStatus(status))
                .and(AuctionSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(AuctionSpecification.hasStartTimeBetween(startTime, endTime));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        Page<Auction>auctions = auctionRepository.findAll(spec, pageable);
        return auctions;
    }


    @Override
    @Scheduled(fixedRate = 60000) // update auction status open -> close every 1 minute
    @Transactional
    public void updateAuctionStatusOpenToClose() {
        log.warn("Start update auction status open to close");
        LocalDateTime now = LocalDateTime.now();
        List<Auction> expiredAuctions = auctionRepository.findByEndTimeBeforeAndStatus(now, AuctionStatus.OPEN);
        for (Auction auction : expiredAuctions) {
            log.warn("Close auction id: " + auction.getId());
            closeAuction(auction.getId());
            messagingTemplate.convertAndSend("/topic/auction-status", auction);
        }
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 86400000) // Update 1 time each day
    public void syncBidCountOfAuction() {
        log.warn("Start auto calculate bid count");
        List<Auction> auctions = auctionRepository.findAll();
        if (auctions.isEmpty()) return;

        for (Auction auction : auctions) {
            long bidCount = bidService.getBidCountOfAuction(auction.getId());

            if (bidCount != auction.getBidCount()){
                auction.setBidCount(bidCount);
                auctionRepository.save(auction);
            }
        }
    }

    // Auto open auction : ready to open
    @Scheduled(fixedRate = 60000)
    public void autoOpenAuctions() {
        List<Auction> readyAuctions = auctionRepository.findByStatus(AuctionStatus.READY);
        for (Auction auction : readyAuctions) {
            if (auction.getStartTime().isBefore(LocalDateTime.now())) {
                auction.setStatus(AuctionStatus.OPEN);
                auctionRepository.save(auction);
                log.info("Auction {} is now OPEN.", auction.getId());
            }
        }
    }


    @Override
    @Transactional
    public AuctionDto createAuction(AuctionCreateRequest auctionCreateRequest) throws Exception {
        if (auctionCreateRequest.getStartTime().isBefore(LocalDateTime.now().plusMinutes(30))) {
            log.error("Time start auction must be at least 30 minutes after the current time");
            throw new AppException(ErrorCode.AUCTION_CREATION_FAILED);
        }

        Product product;

        // Kiểm tra xem sản phẩm đã tồn tại hay chưa
        if (auctionCreateRequest.getProductCreateRequest().getProductId() != null) {
            // Lấy sản phẩm từ cơ sở dữ liệu
            product = productRepository.findById(auctionCreateRequest.getProductCreateRequest().getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            // Kiểm tra trạng thái sản phẩm
            if (product.getStatus() != ProductStatus.INACTIVE) {
                log.error("Product is not in an INACTIVE state");
                throw new AppException(ErrorCode.PRODUCT_NOT_AVAILABLE);
            }
        } else {
            // Nếu không có productId, tạo một sản phẩm mới
            product = productService.createProduct(auctionCreateRequest.getProductCreateRequest());
        }

        // Tạo phiên đấu giá từ DTO và product đã được lưu
        Auction auction = auctionMapper.auctionCreateToAuction(auctionCreateRequest, product);
        auction = auctionRepository.save(auction);

        AuctionDto auctionDto = auctionMapper.auctionToAuctionDto(auction);
        return auctionDto;
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
            log.error("Auction status is not PENDING, current status: {}", auction.getStatus());
            throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
        }

        if (auction.getStartTime().isAfter(LocalDateTime.now())) {
            auction.setStatus(AuctionStatus.READY);
            log.info("Auction {} is READY and will OPEN at: {}", auction.getId(), auction.getStartTime());
        } else {
            auction.setStatus(AuctionStatus.OPEN);
            log.info("Auction {} is now OPEN.", auction.getId());
        }

        Product product = auction.getProduct();
        if (product == null || product.getStatus() != ProductStatus.INACTIVE) {
            log.error("Product is in invalid state. Product status: {}",
                    product != null ? product.getStatus() : "null");
            throw new AppException(ErrorCode.AUCTION_OPEN_FAILED);
        }
        product.setStatus(ProductStatus.ACTIVE);

        // Không cần gọi save nếu sử dụng Spring Data JPA + Hibernate với annotation @Transaction
//        auctionRepository.save(auction);
//        productRepository.save(product);

        log.info("Auction {} is now OPEN. Product {} is ACTIVE.",
                auction.getId(), product.getId());
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
//                throw new AppException(ErrorCode.AUCTION_NOT_HAVE_BID);
                log.warn("Auction no have winner");
                return;
            }


            Bid bid = winBid.get();
            auction.setWinner(bid.getUser().getEmail());
            OrderDto orderDto = OrderDto.builder()
                    .userId(bid.getUser().getId())
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

            // Cancel order
            Optional<Order> order = orderRepository.findByAuctionId(auction.getId());
            if (order.isPresent() && order.get().getStatus() == OrderStatus.PENDING) {
                Order orderValue = order.get();
                orderValue.setStatus(OrderStatus.CANCELED);
                orderRepository.save(orderValue);
            }
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
