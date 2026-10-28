package com.banking.api.service;
import com.banking.api.dto.*;
import com.banking.api.exception.*;
import com.banking.api.model.*;
import com.banking.api.repository.AccountRepository;
import com.banking.api.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = new Account(
            UUID.randomUUID().toString(),
            request.accountHolder(),
            request.initialBalance()
        );
        accountRepository.save(account);
        return toResponse(account);
    }

    public AccountResponse getAccount(String id) {
        return toResponse(findAccount(id));
    }

    public TransactionResponse transfer(TransferRequest request) {
        if (request.fromAccountID().equals(request.toAccountID())) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }

        Account from = findAccount(request.fromAccountID());
        Account to = findAccount(request.toAccountID());

        Account first = from.getId().compareTo(to.getId()) < 0 ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.getBalance().compareTo(request.amount()) < 0) {
                    throw new InsufficientFundsException(from.getId());
                }
                from.withdraw(request.amount());
                to.deposit(request.amount());

                Transaction tx = new Transaction(
                    UUID.randomUUID().toString(),
                    from.getId(),
                    to.getId(),
                    request.amount(),
                    Instant.now()
                );
                transactionRepository.save(tx);
                return toResponse(tx);
            }
        }
    }

    public List<TransactionResponse> getTransactions(String accountID) {
        findAccount(accountID);
        List<TransactionResponse> result = new ArrayList<>();
        for (Transaction t : transactionRepository.findByAccountId(accountID)) {
            result.add(toResponse(t));
        }
        result.sort(Comparator.comparing(TransactionResponse::timestamp).reversed());
        return result;
    }

    private Account findAccount(String id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private AccountResponse toResponse(Account a) {
        return new AccountResponse(
            a.getId(),
            a.getAccountHolder(),
            a.getBalance()
        );
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
            t.id(),
            t.fromAccountID(),
            t.toAccountID(),
            t.amount(),
            t.timestamp()
        );
    }
}
