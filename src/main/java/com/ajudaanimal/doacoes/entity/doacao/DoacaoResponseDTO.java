package com.ajudaanimal.doacoes.entity.doacao;

public record DoacaoResponseDTO(
        String titulo,
        String descricao,
        Categoria categoria,
        EstadoConservacao estadoConservacao,
        String estado,
        String cidade,
        byte[] imagem
) {}
