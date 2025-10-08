package com.ajudaanimal.doacoes.entity.usuario_ong;

import java.util.Date;

public record UsuarioDTO(
        String nome,
        String email,
        String senha,
        String telefone,
        String rua,
        String cidade,
        String estado,
        String bairro,
        TipoDeConta tipoDeConta,
        Date dataCadastro
) {
}
