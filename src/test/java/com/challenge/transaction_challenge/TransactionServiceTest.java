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
	private Transaction parentCarTransaction1, carTransaction2, carTransaction3,
			shoppingTransaction,
			foodTransaction;

	@BeforeEach
	void setUp() {
		repository = new InMemoryTransactionRepository();
		transactionService = new TransactionServiceImpl(repository);
		parentCarTransaction1 = new Transaction(1L, "cars", 1.5, null);
		carTransaction2 = new Transaction(2L, "cars", 3.5, 1L);
		carTransaction3 = new Transaction(3L, "cars", 5.0, 2L);
		shoppingTransaction = new Transaction(10L, "shopping", 5.8, null);
		foodTransaction = new Transaction(20L, "food", 10.2, null);
	}

	@Test
	void testCreateTransaction(){

		transactionService.createTransaction(parentCarTransaction1);

		Optional<Transaction> savedTransaction = repository.findById(1L);

		assertThat(savedTransaction)
				.isPresent()
				.contains(parentCarTransaction1);
	}

	@Test
	void testCreateTransaction_WithParent(){

		transactionService.createTransaction(parentCarTransaction1);
		transactionService.createTransaction(carTransaction2);

		Optional<Transaction> savedTransaction = repository.findById(2L);

		assertThat(savedTransaction)
				.isPresent()
				.contains(carTransaction2)
				.get()
				.extracting(Transaction::getParentId)
				.isEqualTo(1L);

		Optional<Transaction> savedParent = repository.findById(1L);
		assertThat(savedParent).isPresent().contains(parentCarTransaction1);

	}

	@Test
	void testFindByType_OnlyOneFoodType(){
		transactionService.createTransaction(parentCarTransaction1);
		transactionService.createTransaction(carTransaction2);
		transactionService.createTransaction(shoppingTransaction);
		transactionService.createTransaction(foodTransaction);

		List<Long> foodTransactions = transactionService.findByType("food");

		assertThat(foodTransactions)
				.hasSize(1)
				.containsExactlyInAnyOrder(20L)
				.doesNotContain(1L, 2L, 10L);
	}

	@Test
	void testFindByType_2Cars(){
		transactionService.createTransaction(parentCarTransaction1);
		transactionService.createTransaction(carTransaction2);
		transactionService.createTransaction(shoppingTransaction);
		transactionService.createTransaction(foodTransaction);

		List<Long> carsTransactions = transactionService.findByType("cars");

		assertThat(carsTransactions)
				.hasSize(2)
				.containsExactlyInAnyOrder(1L, 2L)
				.doesNotContain(10L, 20L);
	}

	@Test
	void testFindByType_emptyResult(){
		transactionService.createTransaction(parentCarTransaction1);
		transactionService.createTransaction(carTransaction2);
		transactionService.createTransaction(shoppingTransaction);
		transactionService.createTransaction(foodTransaction);

		List<Long> travelTransactions = transactionService.findByType("travel");

		assertThat(travelTransactions)
				.hasSize(0)
				.doesNotContain(1L, 2L, 10L, 20L);
	}

	@Test
	void testSum_SingleTransaction(){
		transactionService.createTransaction(parentCarTransaction1);

		Double sumTransactionAmount = transactionService.sum(1L);

		assertThat(sumTransactionAmount).isEqualTo(1.5);
	}

	@Test
	void testSum_TransactionWithChildren(){
		transactionService.createTransaction(parentCarTransaction1);
		transactionService.createTransaction(carTransaction2);

		Double sumTransactionAmount = transactionService.sum(1L);

		assertThat(sumTransactionAmount).isEqualTo(5.0);
	}

	@Test
	void testSum_TransactionWithGrandchildren(){
		transactionService.createTransaction(parentCarTransaction1);
		transactionService.createTransaction(carTransaction2);
		transactionService.createTransaction(carTransaction3);

		Double sumTransactionAmount = transactionService.sum(1L);

		assertThat(sumTransactionAmount).isEqualTo(10.0);
	}

}
