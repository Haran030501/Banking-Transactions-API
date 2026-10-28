package com.banking.api.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TransferRequest (
    @NotBlank String fromAccountID,
    @NotBlank String toAccountID,
    @NotNull @Positive BigDecimal amount
) {}
