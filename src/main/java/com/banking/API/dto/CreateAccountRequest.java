package com.banking.API.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateAccountRequest (
    @NotBlank String accountHolder,
    @NotNull @PositiveOrZero BigDecimal initialBalance
) {}
