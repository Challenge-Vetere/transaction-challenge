package com.challenge.transaction_challenge.repository.impl;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<Long, Transaction> savedTransactions = new HashMap<>();

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(savedTransactions.get(id));
    }

    @Override
    public void create(Transaction transaction) {
        savedTransactions.put(transaction.getTransactionId(), transaction);
    }
}
