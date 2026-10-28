package com.banking.api.repository;
import com.banking.api.model.Account;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(String id);
}
