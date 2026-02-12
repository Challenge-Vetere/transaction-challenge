package com.challenge.transaction_challenge.repository.impl;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Transaction> findByType(String type) {
        return savedTransactions.values()
                .stream().filter(tx -> tx.getType().equals(type))

                .collect(Collectors.toList());
    }
}
