package com.ajudaanimal.doacoes.entity.usuario_ong;

import lombok.Getter;

@Getter
public enum TipoDeConta {
    USUARIO("USUARIO"),
    ONG("ONG"),
    ADMIN("ADMIN");

    // Terá um link para o usuário comum se cadastrar
    // terá um link para a ONG se cadastrar
    private final String tipo;

    TipoDeConta(String tipo) {
        this.tipo = tipo;
    }
}
