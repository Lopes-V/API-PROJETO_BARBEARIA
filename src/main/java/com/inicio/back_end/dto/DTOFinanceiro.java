package com.inicio.back_end.dto;

import com.inicio.back_end.model.enums.FormasPagamento;
import com.inicio.back_end.model.enums.StatusLancamento;
import com.inicio.back_end.model.enums.TipoLancamento;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DTOFinanceiro {
    private Long id;
    private LocalDate dataLancamento;
    private String descricao;
    private TipoLancamento tipoLancamento;
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    private StatusLancamento statusLancamento;
    private FormasPagamento formasPagamento;
}