package com.inicio.back_end.dto;

import com.inicio.back_end.model.enums.StatusAgendamento;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class DTOAgendamento {
    @NotNull(message = "nao pode ser nulo")
    String nomeCliente;
    @NotNull(message = "nao pode ser nulo")
    Long barbeiroId;
    @NotNull(message = "nao pode ser nulo")
    Long serviceId;
    @Future(message = "nao pode estar no futuro")
    LocalDateTime dataInicio;

    LocalDateTime dataFim;
    @NotNull(message = "nao poder ser nulo")
    StatusAgendamento statusAgendamento;
}