package com.banking.api.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountID) {
        super("Insufficient funds in account: " + accountID);
    }
}
