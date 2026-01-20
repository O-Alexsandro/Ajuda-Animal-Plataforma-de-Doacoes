package com.ajudaanimal.doacoes.entity.usuario_ong;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetSenhaDTO(
        @NotNull @NotBlank String nome,
        @NotNull @NotBlank @Email String email,
        @NotNull @NotBlank @Size(min = 8, max = 128) String novaSenha
) {}

