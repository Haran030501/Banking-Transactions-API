package com.banking.api.controller;
import com.banking.api.dto.TransactionResponse;
import com.banking.api.dto.TransferRequest;
import com.banking.api.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    
    private final BankService bankService;

    public TransferController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse transfer(@Valid @RequestBody TransferRequest request) {
        return bankService.transfer(request);
    }
}
