package com.challenge.transaction_challenge.model;

import lombok.Value;

@Value
public class Transaction {
    Long transactionId;
    String type;
    Double amount;
    Long parentId;
}
