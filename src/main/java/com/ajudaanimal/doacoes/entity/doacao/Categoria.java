package com.ajudaanimal.doacoes.entity.doacao;

public enum Categoria {
    RAÇÃO("Ração"),
    MEDICAMENTOS("Medicamentos"),
    ACESSORIOS("Acessorios"),
    OUTROS("Outros");

    private final String categoria;

    Categoria(String categoria) {
        this.categoria = categoria;
    }
}
