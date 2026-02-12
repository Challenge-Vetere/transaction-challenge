package com.challenge.transaction_challenge.repository;

import com.challenge.transaction_challenge.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findById(Long id);
    void create(Transaction transaction);
    List<Transaction> findByType(String type);
}
