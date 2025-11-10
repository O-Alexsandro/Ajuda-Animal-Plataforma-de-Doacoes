package com.ajudaanimal.doacoes.entity.interesse;

import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import jakarta.validation.constraints.NotNull;

public record InteresseDTO(
        @NotNull
        Long usuarioId,
        @NotNull
        Long doacaoId
) {}
