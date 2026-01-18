package com.ajudaanimal.doacoes.entity.interesse;

import com.ajudaanimal.doacoes.entity.doacao.DoacaoResumoDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.TipoDeConta;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioResumoDTO;

import java.time.LocalDateTime;

public record InteresseResponseDTO(
        Long interesseId,
        Long usuarioId,
        String nome,
        String email,
        String telefone,
        TipoDeConta tipoDeConta,
        DoacaoResumoDTO doacaoResumo,
        UsuarioResumoDTO criadorDoacao,
        StatusInteresse statusInteresse,
        LocalDateTime dataInteresse

) {}
