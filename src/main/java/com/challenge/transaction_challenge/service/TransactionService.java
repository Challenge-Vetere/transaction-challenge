package com.challenge.transaction_challenge.service;

import com.challenge.transaction_challenge.model.Transaction;

import java.util.List;

public interface TransactionService {
    void createTransaction(Transaction transaction);
    List<Long> findByType(String type);
}
