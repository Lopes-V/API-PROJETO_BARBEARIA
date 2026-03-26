package com.inicio.back_end.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
public class DTOBarbeiro {
    @NotNull
    String nome;
    @NotNull
    String especialidade;
    @NotNull
    @Max(value = 100)
    @Min(value = 0)
    BigDecimal comissao;
    @NotNull
    boolean ativo;
}
