package com.banking.api.controller;
import com.banking.api.dto.*;
import com.banking.api.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final BankService bankService;

    public AccountController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return bankService.createAccount(request);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable("id") String id) {
        return bankService.getAccount(id);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getTransactions(@PathVariable("id") String id) {
        return bankService.getTransactions(id);
    }
    
}
