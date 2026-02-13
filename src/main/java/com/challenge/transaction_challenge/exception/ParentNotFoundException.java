package com.challenge.transaction_challenge.exception;

public class ParentNotFoundException extends RuntimeException {

    public ParentNotFoundException(Long id) {
        super("Parent transaction not found with id " + id);
    }
}
