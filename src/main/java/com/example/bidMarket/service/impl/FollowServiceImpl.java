package com.example.bidMarket.service.impl;

import com.example.bidMarket.dto.FollowDto;
import com.example.bidMarket.model.Follow;
import com.example.bidMarket.repository.FollowRepository;
import com.example.bidMarket.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    public FollowDto followSeller(UUID followerId, UUID sellerId) {
        if (followRepository.existsByFollowerIdAndSellerId(followerId, sellerId)) {
            throw new IllegalStateException("Already following this seller.");
        }

        Follow sellerFollow = Follow.builder()
                .followerId(followerId)
                .sellerId(sellerId)
                .followedAt(LocalDateTime.now())
                .build();

        followRepository.save(sellerFollow);
        return new FollowDto(followerId, sellerId, sellerFollow.getFollowedAt());
    }

    @Override
    public boolean isFollowing(UUID followerId, UUID sellerId) {
        return followRepository.existsByFollowerIdAndSellerId(followerId, sellerId);
    }

    @Override
    public void unfollowSeller(UUID followerId, UUID sellerId) {
        Follow follow = followRepository
                .findByFollowerIdAndSellerId(followerId, sellerId)
                .orElseThrow(() -> new IllegalStateException("Not following this seller."));

        followRepository.delete(follow);
    }
}
