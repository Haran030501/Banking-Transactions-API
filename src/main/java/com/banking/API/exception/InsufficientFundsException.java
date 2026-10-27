package com.banking.API.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String accountID) {
        super("Insufficient funds in account: " + accountID);
    }
}
