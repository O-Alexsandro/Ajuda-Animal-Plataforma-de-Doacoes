package com.ajudaanimal.doacoes.entity.doacao;

import jakarta.validation.constraints.NotNull;

public record AtualizarDoacaoDTO(
        @NotNull
        Long id,
        @NotNull
        Long usuarioId,
        String titulo,
        String descricao,
        Categoria categoria,
        EstadoConservacao estadoConservacao,
        String estado,
        String cidade
) {}
