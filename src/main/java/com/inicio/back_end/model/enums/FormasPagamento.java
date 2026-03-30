package com.inicio.back_end.model.enums;

import lombok.Getter;

@Getter
public enum FormasPagamento {
    DEBITO("debito"),
    CREDITO("credito"),
    PIX("pix"),
    BOLETO("boleto");

    private final String descricao;

    FormasPagamento(String descricao) {
        this.descricao = descricao;
    }
}
