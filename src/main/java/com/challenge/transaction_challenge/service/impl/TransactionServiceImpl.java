package com.challenge.transaction_challenge.service.impl;

import com.challenge.transaction_challenge.exception.ParentNotFoundException;
import com.challenge.transaction_challenge.exception.TransactionNotFoundException;
import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.model.TransactionRequest;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import com.challenge.transaction_challenge.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void createByTransactionRequest(Long id, TransactionRequest request) {
        log.info("Creating transaction with id {}", id);
        validateExistingParent(request.getParentId());
        createTransaction(
                new Transaction(
                        id,
                        request.getType(),
                        request.getAmount(),
                        request.getParentId()
                )
        );
    }

    private void validateExistingParent(Long parentId){
        if (!Objects.isNull(parentId) && transactionRepository.findById(parentId).isEmpty()) {
            log.error("Parent transaction not found. parentId={}", parentId);
            throw new ParentNotFoundException(parentId);
        }
    }
    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.create(transaction);
    }

    @Override
    public List<Long> findByType(String type) {
        log.info("Retrieving transactions by type {}", type);
        return transactionRepository.findByType(type)
                .stream().map(Transaction::getTransactionId)
                .collect(Collectors.toList());
    }

    @Override
    public Double sum(Long transactionId) {
        log.info("Calculating sum for transactionId {}", transactionId);

        Transaction parentTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> {
                    log.error("Transaction not found for id {}", transactionId);
                    throw new TransactionNotFoundException(transactionId);
                });

        Double parentAmount = parentTransaction.getAmount();
        Double childrenSum = transactionRepository.findByParentId(transactionId)
                .stream().mapToDouble(childTx -> sum(childTx.getTransactionId()))
                .sum();
        return parentAmount + childrenSum;
    }
}
