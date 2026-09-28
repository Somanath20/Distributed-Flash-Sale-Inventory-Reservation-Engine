package com.somanath.flashstock.dto;
import jakarta.validation.constraints.*;
public record ReservationRequest(@NotBlank String sku,@Min(1) @Max(20) int quantity,@NotBlank String idempotencyKey) {}
