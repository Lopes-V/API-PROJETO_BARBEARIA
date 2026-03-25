package com.inicio.back_end.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DTOBarbeiro (
        @NotNull
        String nome,
        @NotNull
        String especialidade,
        @NotNull
        @Max(value = 100)
        @Min(value = 0)
        BigDecimal comissao,
        @NotNull
        boolean ativo
)
{}
