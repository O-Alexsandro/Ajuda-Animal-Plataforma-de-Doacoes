package com.ajudaanimal.doacoes.entity.usuario_ong;

public record UsuarioResumoDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String rua,
        String cidade,
        String estado,
        String bairro,
        TipoDeConta tipoDeConta
        ) {}

