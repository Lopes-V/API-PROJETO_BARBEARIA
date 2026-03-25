package com.inicio.back_end.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private long id;
    @Column(name = "nome_servico")
    private String nome;
    @Column(name = "preco_servico")
    private BigDecimal preco;
    @Column(name = "duracao_servico")
    private LocalTime duracaoServico;
}
