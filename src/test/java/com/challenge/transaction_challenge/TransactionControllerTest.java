package com.challenge.transaction_challenge;

import com.challenge.transaction_challenge.controllers.TransactionController;
import com.challenge.transaction_challenge.model.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateTransaction() {
        TransactionRequest request = new TransactionRequest("cars", 10.0, null);

        ResponseEntity<Map<String, String>> response = restTemplate
                .exchange("/transactions/1", HttpMethod.PUT, new HttpEntity<>(request),
                        new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("status", "ok");
    }

    @Test
    void testGetTransactionsByType() {
        restTemplate.exchange("/transactions/10", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("food", 10.0, null)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/11", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("food", 20.0, null)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/20", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("cars", 30.0, null)),
                new ParameterizedTypeReference<>() {});

        ResponseEntity<List<Long>> response = restTemplate.exchange("/transactions/types/food",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(10L, 11L);
    }

    @Test
    void testGetTransactionsByType_NonexistentType() {
        restTemplate.exchange("/transactions/10", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("food", 10.0, null)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/20", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("cars", 30.0, null)),
                new ParameterizedTypeReference<>() {});

        ResponseEntity<List<Long>> emptyResponse = restTemplate.exchange("/transactions/types/travel",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});

        assertThat(emptyResponse.getBody()).isEmpty();
    }
}
