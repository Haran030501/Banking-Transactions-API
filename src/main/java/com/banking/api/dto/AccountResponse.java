package com.banking.api.dto;
import java.math.BigDecimal;

public record AccountResponse (
    String id,
    String accountHolder,
    BigDecimal balance
) {}
