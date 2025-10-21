package com.ajudaanimal.doacoes.entity.doacao;

public enum EstadoConservacao {
    NOVO("Novo"),
    USADO("Usado"),
    EM_BOM_ESTADO("Em bom estado");

    private final String estadoConservacao;

    EstadoConservacao(String estadoConservacao) {
        this.estadoConservacao = estadoConservacao;
    }
}
