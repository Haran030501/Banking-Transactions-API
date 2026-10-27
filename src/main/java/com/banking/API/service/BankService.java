package com.banking.API.service;
import com.banking.API.dto.*;
import com.banking.API.exception.*;
import com.banking.API.model.*;
import com.banking.API.repository.AccountRepository;
import com.banking.API.repository.TransactionRepository;
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

        Account first = from.getID().compareTo(to.getID()) < 0 ? from : to;
        Account second = (first == from) ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.getBalance().compareTo(request.amount()) < 0) {
                    throw new InsufficientFundsException(from.getID());
                }
                from.withdraw(request.amount());
                to.deposit(request.amount());

                Transaction tx = new Transaction(
                    UUID.randomUUID().toString(),
                    from.getID(),
                    to.getID(),
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
        for (Transaction t : transactionRepository.findByAccountID(accountID)) {
            result.add(toResponse(t));
        }
        result.sort(Comparator.comparing(TransactionResponse::timestamp).reversed());
        return result;
    }

    private Account findAccount(String ID) {
        return accountRepository.findByID(ID)
            .orElseThrow(() -> new AccountNotFoundException(ID));
    }

    private AccountResponse toResponse(Account a) {
        return new AccountResponse(
            a.getID(),
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
