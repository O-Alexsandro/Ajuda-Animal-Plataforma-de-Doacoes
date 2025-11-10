package com.ajudaanimal.doacoes.entity.interesse;

import com.ajudaanimal.doacoes.entity.doacao.Categoria;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.EstadoConservacao;
import com.ajudaanimal.doacoes.entity.usuario_ong.TipoDeConta;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;

import java.time.LocalDateTime;

public record InteresseResponseDTO(
        Long usuarioId,
        String nome,
        String email,
        String telefone,
        TipoDeConta tipoDeConta,
        Long doacaoId,
        String titulo,
        String descricao,
        Categoria categoria,
        EstadoConservacao estadoConservacao,
        StatusInteresse statusInteresse,
        LocalDateTime dataInteresse

) {}
