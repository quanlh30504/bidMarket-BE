package com.example.bidMarket.service;

import com.example.bidMarket.dto.PayoutDto;
import com.example.bidMarket.mapper.PayoutMapper;
import com.example.bidMarket.model.Payout;
import com.example.bidMarket.repository.PayoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PayoutService {

    private final PayoutRepository payoutRepository;
    private final PayoutMapper payoutMapper;

    public PayoutService(PayoutRepository payoutRepository, PayoutMapper payoutMapper) {
        this.payoutRepository = payoutRepository;
        this.payoutMapper = payoutMapper;
    }

    public PayoutDto createPayout(PayoutDto payoutDto) {
        Payout payout = payoutMapper.payoutDtoToPayout(payoutDto);
        payout = payoutRepository.save(payout);
        return payoutMapper.payoutToPayoutDto(payout);
    }

    public PayoutDto getPayoutById(UUID id) {
        return payoutRepository.findById(id)
                .map(payoutMapper::payoutToPayoutDto)
                .orElseThrow(() -> new RuntimeException("Payout not found"));
    }

    public List<PayoutDto> getAllPayouts() {
        return payoutRepository.findAll().stream()
                .map(payoutMapper::payoutToPayoutDto)
                .collect(Collectors.toList());
    }

    public PayoutDto updatePayout(UUID id, PayoutDto payoutDto) {
        Payout payout = payoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payout not found"));
        payoutMapper.payoutDtoToPayout(payoutDto);  // Cập nhật thông tin từ DTO
        payout = payoutRepository.save(payout);
        return payoutMapper.payoutToPayoutDto(payout);
    }

    public void deletePayout(UUID id) {
        payoutRepository.deleteById(id);
    }
}
