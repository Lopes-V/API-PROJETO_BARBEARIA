package com.inicio.back_end.model.enums;

import lombok.Getter;

@Getter
public enum StatusAgendamento {
    PENDENTE("pendente"),
    CONCLUIDO("concluído"),
    CANCELADO("cancelado"),
    ATRASADO("atrasado");

    private final String descricao;
    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }
}

