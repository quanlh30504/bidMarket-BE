package com.example.bidMarket.controller;

import com.example.bidMarket.dto.PayoutDto;
import com.example.bidMarket.service.PayoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payouts")
public class PayoutController {

    private final PayoutService payoutService;

    public PayoutController(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    @PostMapping
    public ResponseEntity<PayoutDto> createPayout(@RequestBody PayoutDto payoutDto) {
        PayoutDto createdPayout = payoutService.createPayout(payoutDto);
        return ResponseEntity.ok(createdPayout);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayoutDto> getPayoutById(@PathVariable UUID id) {
        PayoutDto payoutDto = payoutService.getPayoutById(id);
        return ResponseEntity.ok(payoutDto);
    }

    @GetMapping
    public ResponseEntity<List<PayoutDto>> getAllPayouts() {
        List<PayoutDto> payouts = payoutService.getAllPayouts();
        return ResponseEntity.ok(payouts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayoutDto> updatePayout(@PathVariable UUID id, @RequestBody PayoutDto payoutDto) {
        PayoutDto updatedPayout = payoutService.updatePayout(id, payoutDto);
        return ResponseEntity.ok(updatedPayout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayout(@PathVariable UUID id) {
        payoutService.deletePayout(id);
        return ResponseEntity.ok().build();
    }
}
