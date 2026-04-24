package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.model.Servico;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T09:59:03-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ServicoMapperImpl implements ServicoMapper {

    @Override
    public Servico toEntity(DTOServico dto) {
        if ( dto == null ) {
            return null;
        }

        Servico.ServicoBuilder servico = Servico.builder();

        if ( dto.getId_servico() != null ) {
            servico.id( dto.getId_servico() );
        }
        servico.duracaoServico( dto.getDuracaoServico() );
        servico.nome( dto.getNome() );
        servico.preco( dto.getPreco() );

        return servico.build();
    }

    @Override
    public DTOServico toDto(Servico entity) {
        if ( entity == null ) {
            return null;
        }

        DTOServico dTOServico = new DTOServico();

        dTOServico.setId_servico( entity.getId() );
        dTOServico.setDuracaoServico( entity.getDuracaoServico() );
        dTOServico.setNome( entity.getNome() );
        dTOServico.setPreco( entity.getPreco() );

        return dTOServico;
    }
}
