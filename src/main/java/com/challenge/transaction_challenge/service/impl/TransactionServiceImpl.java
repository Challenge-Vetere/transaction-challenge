package com.challenge.transaction_challenge.service.impl;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import com.challenge.transaction_challenge.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.create(transaction);
    }
}
