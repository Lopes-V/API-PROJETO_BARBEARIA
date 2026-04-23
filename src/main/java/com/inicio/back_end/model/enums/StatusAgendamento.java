package com.inicio.back_end.model.enums;

import lombok.Getter;

@Getter
public enum StatusAgendamento {
    PENDENTE("PENDENTE"),
    CONCLUIDO("CONCLUIDO"),
    CANCELADO("CANCELADO"),
    ATRASADO("ATRASADO");

    private final String descricao;
    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }
}

