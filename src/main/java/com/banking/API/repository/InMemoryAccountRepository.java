package com.banking.API.repository;
import com.banking.API.model.Account;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository 
public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(account.getID(), account);
        return account;
    }

    @Override
    public Optional<Account> findByID(String ID) {
        return Optional.ofNullable(accounts.get(ID));
    }
}
