package com.challenge.transaction_challenge.controllers;

import com.challenge.transaction_challenge.model.TransactionRequest;
import com.challenge.transaction_challenge.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{transactionId}")
    public ResponseEntity<Map<String,String>> create(@PathVariable Long transactionId, @RequestBody TransactionRequest request){
        transactionService.createByTransactionRequest(transactionId, request);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/types/{type}")
    public List<Long> getByType(@PathVariable String type){
        return transactionService.findByType(type);
    }

    @GetMapping("/sum/{transactionId}")
    public Map<String,Double> getSum(@PathVariable Long transactionId){
        Double sum = transactionService.sum(transactionId);
        return Map.of("sum", sum);
    }

}
