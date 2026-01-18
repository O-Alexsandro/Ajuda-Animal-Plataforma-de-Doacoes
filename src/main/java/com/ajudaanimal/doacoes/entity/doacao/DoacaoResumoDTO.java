package com.ajudaanimal.doacoes.entity.doacao;

import java.time.LocalDateTime;
import java.util.List;

public record DoacaoResumoDTO(
        Long id,
        String titulo,
        String descricao,
        Categoria categoria,
        EstadoConservacao estadoConservacao,
        String estado,
        String cidade,
        Status status,
        List<byte[]> imagens,
        LocalDateTime dataCadastro
) {}
