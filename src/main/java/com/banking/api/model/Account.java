package com.banking.api.model;
import java.math.BigDecimal;

public class Account {
    private final String id;
    private final String accountHolder;
    private BigDecimal balance;
    
    public Account(String id, String accountHolder, BigDecimal balance){
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public String getId() { return id; }
    public String getAccountHolder() { return accountHolder; }
    public BigDecimal getBalance() { return balance; }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
}
