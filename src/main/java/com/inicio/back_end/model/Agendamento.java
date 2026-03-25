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
    @ManyToOne
    @JoinColumn(name = "id_barbeiro")
    private Barbeiro barbeiro;
    @ManyToOne
    @JoinColumn(name = "id_servico")
    private Servico servico;
    @Column(name = "data_inicio_agendamento")
    private LocalDateTime dataInicio;
    @Column(name = "data_fim_agendamento")
    private LocalDateTime dataFim;
    @Column(name = "status_agendamento")
    private StatusAgendamento statusAgendamento;
}

//Fora do Expediente: O sistema deve rejeitar horários antes das 08:00 ou após as 20:00 (ou o horário da sua barbearia).
//
//Status Inválido: Um agendamento marcado como CONCLUIDO não pode ser alterado para CANCELADO.