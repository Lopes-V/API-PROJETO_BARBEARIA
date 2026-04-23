package com.inicio.back_end.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    private Long id;
    
    @NotNull(message = "nao pode ser nulo")
    String nomeItem;
    
    @NotNull(message = "nao pode ser nulo")
    @PositiveOrZero(message = "nao pode ser negativo")
    int quantidadeAtual;
    
    @NotNull(message = "nao pode ser nulo")
    @Positive(message = "nao pode ser negativo ou zero")
    int quantidadeMinima;
}
