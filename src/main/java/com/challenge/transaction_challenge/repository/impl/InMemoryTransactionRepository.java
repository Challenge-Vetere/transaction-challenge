package com.challenge.transaction_challenge.repository.impl;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
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
                .stream().filter(tx -> Objects.equals(tx.getType(), type))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByParentId(Long parentId) {
        return savedTransactions.values()
                .stream().filter(tx -> Objects.equals(tx.getParentId(), parentId))
                .collect(Collectors.toList());
    }
}
