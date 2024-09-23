package com.example.bidMarket.repository;

import com.example.bidMarket.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
}

