package com.inicio.back_end.model;


import com.inicio.back_end.model.enums.FormasPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
    @OneToOne
    @JoinColumn(name = "id_agendamento")
    private Agendamento agendamento;
    @Column(name = "valor_total_financeiro")
    private BigDecimal valorTotal;
    @Column(name = "valor_barbeiro")
    private BigDecimal valorBarbeiro;
    @Column(name = "valor_casa")
    private BigDecimal valorCasa;
    @Column(name = "formas_pagamento")
    private FormasPagamento formasPagamento;
}

//Pagamento Duplo: Impedir que um mesmo Agendamento gere dois lançamentos financeiros (evita erro de clique duplo no botão "Finalizar").
//
//Divergência de Valores: A soma do valorBarbeiro + valorCasa deve ser exatamente igual ao valorTotal. Se sobrar ou faltar 1 centavo, o sistema lança uma RegraDeNegocioException