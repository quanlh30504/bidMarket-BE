package com.example.bidMarket.controller;

import com.example.bidMarket.dto.FollowDto;
import com.example.bidMarket.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService sellerFollowService;

    @PostMapping("/{sellerId}/follow")
    public ResponseEntity<FollowDto> followSeller(
            @PathVariable UUID sellerId,
            @RequestParam UUID followerId) {

        FollowDto followDTO = sellerFollowService.followSeller(followerId, sellerId);
        return ResponseEntity.ok(followDTO);
    }

    @DeleteMapping("/{sellerId}/unfollow")
    public ResponseEntity<Void> unfollowSeller(
            @PathVariable UUID sellerId,
            @RequestParam UUID followerId) {

        sellerFollowService.unfollowSeller(followerId, sellerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sellerId}/isFollowing")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable UUID sellerId,
            @RequestParam UUID followerId) {

        boolean isFollowing = sellerFollowService.isFollowing(followerId, sellerId);
        return ResponseEntity.ok(isFollowing);
    }
}
