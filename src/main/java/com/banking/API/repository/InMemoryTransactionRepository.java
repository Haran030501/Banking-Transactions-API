package com.banking.API.repository;
import com.banking.API.model.Transaction;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    @Override
    public Transaction save(Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findByAccountID(String accountID) {
        return transactions.stream()
                .filter(t -> accountID.equals(t.fromAccountID()) || accountID.equals(t.toAccountID()))
                .toList();
    }
}
