package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.model.Agendamento;
import com.inicio.back_end.model.Financeiro;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T09:33:08-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class FinanceiroMapperImpl implements FinanceiroMapper {

    @Override
    public Financeiro toEntity(DTOFinanceiro dto) {
        if ( dto == null ) {
            return null;
        }

        Financeiro.FinanceiroBuilder financeiro = Financeiro.builder();

        financeiro.agendamento( dTOFinanceiroToAgendamento( dto ) );
        financeiro.valorTotal( dto.getValorTotal() );
        financeiro.valorBarbeiro( dto.getValorBarbeiro() );
        financeiro.valorCasa( dto.getValorCasa() );
        financeiro.formasPagamento( dto.getFormasPagamento() );

        return financeiro.build();
    }

    @Override
    public DTOFinanceiro toDto(Financeiro entity) {
        if ( entity == null ) {
            return null;
        }

        DTOFinanceiro dTOFinanceiro = new DTOFinanceiro();

        dTOFinanceiro.setAgendamentoId( entityAgendamentoId( entity ) );
        dTOFinanceiro.setValorTotal( entity.getValorTotal() );
        dTOFinanceiro.setValorBarbeiro( entity.getValorBarbeiro() );
        dTOFinanceiro.setValorCasa( entity.getValorCasa() );
        dTOFinanceiro.setFormasPagamento( entity.getFormasPagamento() );

        return dTOFinanceiro;
    }

    protected Agendamento dTOFinanceiroToAgendamento(DTOFinanceiro dTOFinanceiro) {
        if ( dTOFinanceiro == null ) {
            return null;
        }

        Agendamento.AgendamentoBuilder agendamento = Agendamento.builder();

        agendamento.id( dTOFinanceiro.getAgendamentoId() );

        return agendamento.build();
    }

    private Long entityAgendamentoId(Financeiro financeiro) {
        Agendamento agendamento = financeiro.getAgendamento();
        if ( agendamento == null ) {
            return null;
        }
        return agendamento.getId();
    }
}
