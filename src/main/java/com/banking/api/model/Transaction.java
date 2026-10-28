package com.banking.api.model;
import java.math.BigDecimal;
import java.time.Instant;

public record Transaction (
    String id,
    String fromAccountID,
    String toAccountID,
    BigDecimal amount,
    Instant timestamp
) {}
