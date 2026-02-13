package com.challenge.transaction_challenge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class TransactionRequest {
    @NotBlank(message = "Type is required")
    @Schema(
            description = "Transaction type",
            example = "shopping"
    )
    String type;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @Schema(
            description = "Transaction amount (must be positive)",
            example = "5000"
    )
    Double amount;
    @Schema(
            description = "Optional parent transaction ID (parent must exist)",
            example = "10"
    )
    Long parentId;
}
