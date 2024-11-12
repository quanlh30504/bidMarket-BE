package com.example.bidMarket.repository;

import com.example.bidMarket.model.WatchList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, UUID> {
   Page<WatchList> findAllByUserId(UUID userId, Pageable pageable);
}
