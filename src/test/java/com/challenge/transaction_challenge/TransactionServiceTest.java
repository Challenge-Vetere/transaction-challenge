package com.challenge.transaction_challenge;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import com.challenge.transaction_challenge.repository.impl.InMemoryTransactionRepository;
import com.challenge.transaction_challenge.service.TransactionService;
import com.challenge.transaction_challenge.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionServiceTest {

	private TransactionRepository repository;
	private TransactionService transactionService;
	private Transaction parentTransaction, transaction;

	@BeforeEach
	void setUp() {
		repository = new InMemoryTransactionRepository();
		transactionService = new TransactionServiceImpl(repository);
		parentTransaction = new Transaction(1L, "cars", 1.5, null);
		transaction = new Transaction(2L, "cars", 3.5, 1L);
	}

	@Test
	void testCreateTransaction(){

		transactionService.createTransaction(parentTransaction);

		Optional<Transaction> savedTransaction = repository.findById(1L);

		assertThat(savedTransaction)
				.isPresent()
				.contains(parentTransaction);
	}

	@Test
	void testCreateTransactionWithParent(){

		transactionService.createTransaction(parentTransaction);
		transactionService.createTransaction(transaction);

		Optional<Transaction> savedTransaction = repository.findById(2L);

		assertThat(savedTransaction)
				.isPresent()
				.contains(transaction)
				.get()
				.extracting(Transaction::getParentId)
				.isEqualTo(1L);

		Optional<Transaction> savedParent = repository.findById(1L);
		assertThat(savedParent).isPresent().contains(parentTransaction);

	}

}
