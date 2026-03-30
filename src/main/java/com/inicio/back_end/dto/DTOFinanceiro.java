package com.inicio.back_end.dto;

import com.inicio.back_end.model.enums.FormasPagamento;
import com.inicio.back_end.model.enums.StatusLancamento;
import com.inicio.back_end.model.enums.TipoLancamento;
import jakarta.validation.constraints.NotNull;
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
    private Long agendamentoId;
    private LocalDate dataLancamento;
    private String descricao;
    private TipoLancamento tipoLancamento;
    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;
    private StatusLancamento statusLancamento;
    private FormasPagamento formasPagamento;
}