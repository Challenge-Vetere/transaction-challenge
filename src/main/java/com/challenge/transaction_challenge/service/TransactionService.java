package com.challenge.transaction_challenge.service;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.model.TransactionRequest;

import java.util.List;

public interface TransactionService {
    void createByTransactionRequest(Long id, TransactionRequest request);
    void createTransaction(Transaction transaction);
    List<Long> findByType(String type);
    Double sum(Long transactionId);
}
