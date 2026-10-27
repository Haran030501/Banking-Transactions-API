package com.banking.API.repository;
import com.banking.API.model.Account;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findByID(String ID);
}
