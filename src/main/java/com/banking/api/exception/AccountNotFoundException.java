package com.banking.api.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountID) {
        super("Account not found: " + accountID);
    }
}
