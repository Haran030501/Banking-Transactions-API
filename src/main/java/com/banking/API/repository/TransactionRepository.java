package com.banking.API.repository;
import com.banking.API.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountID(String accountID);
}
