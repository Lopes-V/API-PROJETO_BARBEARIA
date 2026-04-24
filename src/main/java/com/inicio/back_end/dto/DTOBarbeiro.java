package com.inicio.back_end.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class DTOBarbeiro {
    Long id_barbeiro;
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
