package com.ajudaanimal.doacoes.entity.usuario_ong;

import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        @NotNull
        String nome,
        @NotNull
        String email,
        @NotNull
        String senha,
        @NotNull
        String telefone,
        @NotNull
        String rua,
        @NotNull
        String cidade,
        @NotNull
        String estado,
        @NotNull
        String bairro,
        @NotNull
        TipoDeConta tipoDeConta
) {}
