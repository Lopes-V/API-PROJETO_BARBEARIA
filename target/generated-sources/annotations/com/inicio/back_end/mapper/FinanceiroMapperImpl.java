package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.model.Agendamento;
import com.inicio.back_end.model.Financeiro;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T14:14:22-0300",
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
        financeiro.id( dto.getId() );
        financeiro.dataLancamento( dto.getDataLancamento() );
        financeiro.descricao( dto.getDescricao() );
        financeiro.tipoLancamento( dto.getTipoLancamento() );
        financeiro.valor( dto.getValor() );
        financeiro.statusLancamento( dto.getStatusLancamento() );
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
        dTOFinanceiro.setId( entity.getId() );
        dTOFinanceiro.setDataLancamento( entity.getDataLancamento() );
        dTOFinanceiro.setDescricao( entity.getDescricao() );
        dTOFinanceiro.setTipoLancamento( entity.getTipoLancamento() );
        dTOFinanceiro.setValor( entity.getValor() );
        dTOFinanceiro.setStatusLancamento( entity.getStatusLancamento() );
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
