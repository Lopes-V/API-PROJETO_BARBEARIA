package com.inicio.back_end.model;


import com.inicio.back_end.model.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long id;
    @Column(name = "nome_cliente_agendamento")
    String nomeCliente;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_barbeiro")
    private Barbeiro barbeiro;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_servico")
    private Servico servico;
    @Column(name = "data_inicio_agendamento")
    private LocalDateTime dataInicio;
    @Column(name = "data_fim_agendamento")
    private LocalDateTime dataFim;
    @Column(name = "status_agendamento")
    private StatusAgendamento statusAgendamento;
}
