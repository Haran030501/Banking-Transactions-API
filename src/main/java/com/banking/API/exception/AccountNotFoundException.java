package com.banking.API.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountID) {
        super("Account not found: " + accountID);
    }
}
