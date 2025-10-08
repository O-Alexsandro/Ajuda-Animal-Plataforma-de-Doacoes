package com.ajudaanimal.doacoes.entity.usuario_ong;

import jakarta.validation.constraints.NotNull;

public record AtualizarUsuarioDTO(
        @NotNull
        Long id,
        String nome,
        String email,
        String senha,
        String telefone,
        String rua,
        String cidade,
        String estado,
        String bairro
) {}
