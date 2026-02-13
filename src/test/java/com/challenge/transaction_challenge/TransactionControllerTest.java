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
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Test
    void testGetTransactionSum_SingleTransaction() {
        restTemplate.exchange("/transactions/30", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("cars", 30.0, null)),
                new ParameterizedTypeReference<>() {});

        ResponseEntity<Map<String, Double>> response = restTemplate.exchange("/transactions/sum/30",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});

        assertThat(response.getBody()).containsEntry("sum", 30.0);
    }

    @Test
    void testGetTransactionSum_WithChildren() {
        restTemplate.exchange("/transactions/40", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("cars", 40.0, null)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/50", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("shopping", 50.5, null)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/51", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("shopping", 51.5, 50L)),
                new ParameterizedTypeReference<>() {});

        ResponseEntity<Map<String, Double>> responseParent = restTemplate.exchange("/transactions/sum/50",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        assertThat(responseParent.getBody()).containsEntry("sum", 102.0);

        ResponseEntity<Map<String, Double>> responseChild = restTemplate.exchange("/transactions/sum/51",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        assertThat(responseChild.getBody()).containsEntry("sum", 51.5);
    }

    @Test
    void testGetTransactionSum_WithGrandhildren() {
        restTemplate.exchange("/transactions/60", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("travel", 1.0, null)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/61", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("travel", 2.0, 60L)),
                new ParameterizedTypeReference<>() {});
        restTemplate.exchange("/transactions/62", HttpMethod.PUT,
                new HttpEntity<>(new TransactionRequest("travel", 3.0, 61L)),
                new ParameterizedTypeReference<>() {});

        ResponseEntity<Map<String, Double>> responseParent = restTemplate.exchange("/transactions/sum/60",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        assertThat(responseParent.getBody()).containsEntry("sum", 6.0);

        ResponseEntity<Map<String, Double>> responseChild = restTemplate.exchange("/transactions/sum/61",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        assertThat(responseChild.getBody()).containsEntry("sum", 5.0);

        ResponseEntity<Map<String, Double>> responseGrandchild = restTemplate.exchange("/transactions/sum/62",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        assertThat(responseGrandchild.getBody()).containsEntry("sum", 3.0);
    }
}
