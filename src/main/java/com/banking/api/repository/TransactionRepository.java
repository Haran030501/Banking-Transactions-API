package com.banking.api.repository;
import com.banking.api.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountId(String accountID);
}
