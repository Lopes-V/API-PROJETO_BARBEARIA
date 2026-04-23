package com.inicio.back_end.model.enums;

import lombok.Getter;

@Getter
public enum FormasPagamento {
    DEBITO("DEBITO"),
    CREDITO("CREDITO"),
    PIX("PIX"),
    DINHEIRO("DINHEIRO"),
    BOLETO("BOLETO");

    private final String descricao;

    FormasPagamento(String descricao) {
        this.descricao = descricao;
    }
}
