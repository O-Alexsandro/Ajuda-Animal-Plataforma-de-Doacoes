package com.ajudaanimal.doacoes.entity.usuario_ong;

import lombok.Getter;

@Getter
public enum TipoDeConta {
    USUARIO("USUARIO"),
    ONG("ONG"),
    ADMIN("ADMIN");

    private final String tipo;

    TipoDeConta(String tipo) {
        this.tipo = tipo;
    }
}
