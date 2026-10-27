package com.banking.API.dto;
import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    int status,
    String error,
    String message,
    List<String> details,
    Instant timestamp
) {}
