package com.challenge.transaction_challenge.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class TransactionRequest {
    @NonNull
    String type;
    @NonNull
    Double amount;
    Long parentId;
}
