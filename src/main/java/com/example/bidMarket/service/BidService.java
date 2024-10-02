package com.example.bidMarket.service;

import com.example.bidMarket.dto.BidDto;
import com.example.bidMarket.dto.Request.BidCreateRequest;
import com.example.bidMarket.dto.Response.BidCreateResponse;

import java.util.UUID;

public interface BidService {

    BidCreateResponse createBid(BidCreateRequest bidCreateRequest) throws Exception;

    void processBid (BidCreateRequest bidCreateRequest);
}
