package com.ajudaanimal.doacoes.entity.doacao;

import java.time.LocalDateTime;
import java.util.List;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioResumoDTO;

public record DoacaoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        Categoria categoria,
        EstadoConservacao estadoConservacao,
        String estado,
        String cidade,
        String bairro,
        String cep,
        Status status,
        LocalDateTime dataCadastro,
        List<byte[]> imagens,
        UsuarioResumoDTO criador
) {}
