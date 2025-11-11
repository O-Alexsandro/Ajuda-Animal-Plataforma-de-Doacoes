package com.ajudaanimal.doacoes.entity.login;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull
        String email,
        @NotNull
        String senha
) {}