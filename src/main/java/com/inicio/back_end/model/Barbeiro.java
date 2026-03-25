package com.inicio.back_end.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Barbeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barbeiro")
    private Long id;
    @Column(name = "nome_barbeiro", unique = true)
    private String nome;
    @Column(name = "especialidade_barbeiro")
    private String especialidade;
    @Column(name = "percentual_comissao_barbeiro", precision = 5, scale = 2)
    private BigDecimal comissao;
    @Column(name = "status_barbeiro")
    private boolean ativo;
}