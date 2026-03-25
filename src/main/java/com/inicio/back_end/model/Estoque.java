package com.inicio.back_end.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_item_estoque")
    private String nomeItem;
    @Column(name = "quantidade_estoque")
    private int quantidadeAtual;
    @Column(name = "quantidade_minima_estoque")
    private int quantidadeMinima;
}
