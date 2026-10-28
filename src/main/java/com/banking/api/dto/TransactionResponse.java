package com.banking.api.dto;
import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse (
    String id,
    String fromAccountID,
    String toAccountID,
    BigDecimal amount,
    Instant timestamp
) {}
