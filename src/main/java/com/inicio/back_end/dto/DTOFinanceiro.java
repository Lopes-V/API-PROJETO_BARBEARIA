package com.inicio.back_end.dto;

import com.inicio.back_end.model.enums.FormasPagamento;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DTOFinanceiro {
    @NotNull(message = "nao pode ser nulo")
    Long agendamentoId;
    @NegativeOrZero(message = "nao pode ser negativo ou zero")
    @NotNull(message = "nao pode ser nulo")
    BigDecimal valorTotal;
    @NegativeOrZero(message = "nao pode ser negativo ou zero")
    @NotNull(message = "nao pode ser nulo")
    BigDecimal valorBarbeiro;
    @NegativeOrZero(message = "nao pode ser negativo ou zero")
    @NotNull(message = "nao pode ser nulo")
    BigDecimal valorCasa;
    @NegativeOrZero(message = "nao pode ser negativo ou zero")
    @NotNull(message = "nao pode ser nulo")
    FormasPagamento formasPagamento;
}