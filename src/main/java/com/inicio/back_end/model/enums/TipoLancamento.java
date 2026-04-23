package com.inicio.back_end.model.enums;

import lombok.Getter;

@Getter
public enum TipoLancamento {
    RECEITA("RECEITA"),
    DESPESA("DESPESA");

    private final String tipo;

    TipoLancamento(String tipo) {
        this.tipo = tipo;
    }
}
