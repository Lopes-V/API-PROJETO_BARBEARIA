package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.model.Servico;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T09:14:03-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class ServicoMapperImpl implements ServicoMapper {

    @Override
    public Servico toEntity(DTOServico dto) {
        if ( dto == null ) {
            return null;
        }

        Servico.ServicoBuilder servico = Servico.builder();

        servico.nome( dto.getNome() );
        servico.preco( dto.getPreco() );
        servico.duracaoServico( dto.getDuracaoServico() );

        return servico.build();
    }

    @Override
    public DTOServico toDto(Servico entity) {
        if ( entity == null ) {
            return null;
        }

        DTOServico dTOServico = new DTOServico();

        dTOServico.setNome( entity.getNome() );
        dTOServico.setDuracaoServico( entity.getDuracaoServico() );
        dTOServico.setPreco( entity.getPreco() );

        return dTOServico;
    }
}
