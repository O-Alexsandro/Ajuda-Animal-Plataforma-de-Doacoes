package com.ajudaanimal.doacoes.entity.doacao;

public enum Status {
    DISPONIVEL("Disponivel"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluido"),
    EM_NEGOCIACAO("Em negociacao");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
