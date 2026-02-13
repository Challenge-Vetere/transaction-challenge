package com.challenge.transaction_challenge.controllers;

import com.challenge.transaction_challenge.model.TransactionRequest;
import com.challenge.transaction_challenge.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{transactionId}")
    @Operation(
            summary = "Create or update a transaction",
            description = "Stores a transaction with transactionId, amount, type and optional parentId"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction stored successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content)
    })
    public ResponseEntity<Map<String,String>> create(@PathVariable Long transactionId,
                                                     @Valid @RequestBody TransactionRequest request){
        transactionService.createByTransactionRequest(transactionId, request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/types/{type}")
    @Operation(
            summary = "Get transactions by type",
            description = "Returns a list of transaction IDs matching the given type"
    )
    public List<Long> getByType(@PathVariable String type){
        return transactionService.findByType(type);
    }

    @GetMapping("/sum/{transactionId}")
    @Operation(
            summary = "Get transaction sum",
            description = "Calculates the sum of the transaction and all its child transactions"
    )
    public Map<String,Double> getSum(@PathVariable Long transactionId){
        Double sum = transactionService.sum(transactionId);
        return Map.of("sum", sum);
    }

}
