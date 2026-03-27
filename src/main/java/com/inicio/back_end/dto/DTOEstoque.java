package com.inicio.back_end.dto;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DTOEstoque {
    @NotNull(message = "nao pode ser nulo")
    String nomeItem;
    @NotNull(message = "nao pode ser nulo")
    @Negative(message = "nao pode ser negativo")
    int quantidadeAtual;
    @NotNull(message = "nao pode ser nulo")
    @NegativeOrZero(message = "nao pode ser negativo ou zero")
    int quantidadeMinima;
}
