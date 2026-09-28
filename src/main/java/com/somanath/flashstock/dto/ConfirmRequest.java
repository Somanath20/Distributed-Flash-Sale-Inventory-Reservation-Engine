package com.somanath.flashstock.dto;
import jakarta.validation.constraints.NotBlank;
public record ConfirmRequest(@NotBlank String reservationKey) {}
