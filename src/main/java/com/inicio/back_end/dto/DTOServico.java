package com.inicio.back_end.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DTOServico (

        @NotBlank(message = "O nome do serviço é obrigatório")
        String nome,

        @NotNull(message = "A duração deve ser informada")
        @Min(value = 5, message = "Tempo de corte muito curto! O mínimo é 5 minutos.")
        @Max(value = 240, message = "Tempo de corte muito longo! O máximo permitido são 4 horas (240 min).")
        Integer duracaoMinutos,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "O serviço não pode ser de graça!")
        BigDecimal preco
)
{}
