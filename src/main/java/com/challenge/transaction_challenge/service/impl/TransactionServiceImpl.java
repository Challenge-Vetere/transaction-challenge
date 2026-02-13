package com.challenge.transaction_challenge.service.impl;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import com.challenge.transaction_challenge.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.create(transaction);
    }

    @Override
    public List<Long> findByType(String type) {
        return transactionRepository.findByType(type)
                .stream().map(Transaction::getTransactionId)
                .collect(Collectors.toList());
    }

    @Override
    public Double sum(Long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        return transaction.map(Transaction::getAmount).orElse(0.0);
    }
}
