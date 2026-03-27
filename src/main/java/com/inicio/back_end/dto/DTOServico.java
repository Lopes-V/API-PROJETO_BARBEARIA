package com.inicio.back_end.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;


@AllArgsConstructor
@NoArgsConstructor
public class DTOServico {
    @NotBlank(message = "O nome do serviço é obrigatório")
    String nome;
    @Getter
    @Setter

    @NotNull(message = "A duração deve ser informada")
    @Min(value = 5, message = "Tempo de corte muito curto! O mínimo é 5 minutos.")
    @Max(value = 240, message = "Tempo de corte muito longo! O máximo permitido são 4 horas (240 min).")
    LocalTime duracaoServico;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O serviço não pode ser de graça!")
    BigDecimal preco;
}
