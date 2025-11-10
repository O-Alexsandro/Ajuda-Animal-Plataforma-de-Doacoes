package com.ajudaanimal.doacoes.entity.doacao;

import jakarta.validation.constraints.NotNull;

public record DoacaoDTO(
        @NotNull
        Long usuarioId,
        @NotNull
        String titulo,
        @NotNull
        String descricao,
        @NotNull
        Categoria categoria,
        @NotNull
        EstadoConservacao estadoConservacao
) {}
