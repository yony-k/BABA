package org.example.baba.common.security.dto;

import jakarta.validation.constraints.NotNull;

public record LoginDto(@NotNull String email, @NotNull String password) {}
