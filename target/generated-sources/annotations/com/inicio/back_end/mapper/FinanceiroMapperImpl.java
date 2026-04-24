package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.model.Financeiro;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T09:59:38-0300",
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

        dTOFinanceiro.setId( entity.getId() );
        dTOFinanceiro.setDataLancamento( entity.getDataLancamento() );
        dTOFinanceiro.setDescricao( entity.getDescricao() );
        dTOFinanceiro.setTipoLancamento( entity.getTipoLancamento() );
        dTOFinanceiro.setValor( entity.getValor() );
        dTOFinanceiro.setStatusLancamento( entity.getStatusLancamento() );
        dTOFinanceiro.setFormasPagamento( entity.getFormasPagamento() );

        return dTOFinanceiro;
    }
}
