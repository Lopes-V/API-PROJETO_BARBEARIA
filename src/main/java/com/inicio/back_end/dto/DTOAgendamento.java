package com.inicio.back_end.dto;

import com.inicio.back_end.model.enums.StatusAgendamento;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


public record DTOAgendamento (
    Long id,
    @NotNull(message = "nao pode ser nulo")
    String nomeCliente,
    @NotNull(message = "nao pode ser nulo")
    Long barbeiroId,
    @NotNull(message = "nao pode ser nulo")
    Long serviceId,
    @Future(message = "deve ser uma data futura")
    LocalDateTime dataInicio,

    LocalDateTime dataFim,
    @NotNull(message = "nao pode ser nulo")
    StatusAgendamento statusAgendamento)
{}