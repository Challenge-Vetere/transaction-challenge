package com.challenge.transaction_challenge;

import com.challenge.transaction_challenge.model.Transaction;
import com.challenge.transaction_challenge.repository.TransactionRepository;
import com.challenge.transaction_challenge.repository.impl.InMemoryTransactionRepository;
import com.challenge.transaction_challenge.service.TransactionService;
import com.challenge.transaction_challenge.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionServiceTest {

	private TransactionRepository repository;
	private TransactionService transactionService;
	private Transaction parentCarTransaction, carTransaction, shoppingTransaction, foodTransaction;

	@BeforeEach
	void setUp() {
		repository = new InMemoryTransactionRepository();
		transactionService = new TransactionServiceImpl(repository);
		parentCarTransaction = new Transaction(1L, "cars", 1.5, null);
		carTransaction = new Transaction(2L, "cars", 3.5, 1L);
		shoppingTransaction = new Transaction(10L, "shopping", 5.8, null);
		foodTransaction = new Transaction(20L, "food", 10.2, null);
	}

	@Test
	void testCreateTransaction(){

		transactionService.createTransaction(parentCarTransaction);

		Optional<Transaction> savedTransaction = repository.findById(1L);

		assertThat(savedTransaction)
				.isPresent()
				.contains(parentCarTransaction);
	}

	@Test
	void testCreateTransaction_WithParent(){

		transactionService.createTransaction(parentCarTransaction);
		transactionService.createTransaction(carTransaction);

		Optional<Transaction> savedTransaction = repository.findById(2L);

		assertThat(savedTransaction)
				.isPresent()
				.contains(carTransaction)
				.get()
				.extracting(Transaction::getParentId)
				.isEqualTo(1L);

		Optional<Transaction> savedParent = repository.findById(1L);
		assertThat(savedParent).isPresent().contains(parentCarTransaction);

	}

	@Test
	void testFindByType_OnlyOneFoodType(){
		transactionService.createTransaction(parentCarTransaction);
		transactionService.createTransaction(carTransaction);
		transactionService.createTransaction(shoppingTransaction);
		transactionService.createTransaction(foodTransaction);

		List<Long> foodTransactions = transactionService.findByType("food");

		assertThat(foodTransactions)
				.hasSize(1)
				.containsExactlyInAnyOrder(20L)
				.doesNotContain(1L, 2L, 10L);
	}

}
