package com.challenge.transaction_challenge.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class TransactionRequest {
    @NotBlank(message = "Type is required")
    String type;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    Double amount;
    Long parentId;
}
