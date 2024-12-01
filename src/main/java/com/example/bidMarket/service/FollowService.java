package com.example.bidMarket.service;

import com.example.bidMarket.dto.FollowDto;

import java.util.UUID;

public interface FollowService {
    FollowDto followSeller(UUID followerId, UUID sellerId);
    boolean isFollowing(UUID followerId, UUID sellerId);
    void unfollowSeller(UUID followerId, UUID sellerId);
}
