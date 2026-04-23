package com.inicio.back_end.model;

import com.inicio.back_end.model.enums.FormasPagamento;
import com.inicio.back_end.model.enums.StatusLancamento;
import com.inicio.back_end.model.enums.TipoLancamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Financeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_financeiro")
    private Long id;
    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "tipo_lancamento")
    @Enumerated(EnumType.STRING)
    private TipoLancamento tipoLancamento;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "status_lancamento")
    @Enumerated(EnumType.STRING)
    private StatusLancamento statusLancamento;
    @Column(name = "formas_pagamento")
    private FormasPagamento formasPagamento;
}