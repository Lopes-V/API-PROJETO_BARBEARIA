package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOEstoque;
import com.inicio.back_end.model.Estoque;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T09:59:03-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EstoqueMapperImpl implements EstoqueMapper {

    @Override
    public Estoque toEntity(DTOEstoque dto) {
        if ( dto == null ) {
            return null;
        }

        Estoque.EstoqueBuilder estoque = Estoque.builder();

        estoque.id( dto.getId() );
        estoque.nomeItem( dto.getNomeItem() );
        estoque.quantidadeAtual( dto.getQuantidadeAtual() );
        estoque.quantidadeMinima( dto.getQuantidadeMinima() );

        return estoque.build();
    }

    @Override
    public DTOEstoque toDto(Estoque barbeiro) {
        if ( barbeiro == null ) {
            return null;
        }

        DTOEstoque dTOEstoque = new DTOEstoque();

        dTOEstoque.setId( barbeiro.getId() );
        dTOEstoque.setNomeItem( barbeiro.getNomeItem() );
        dTOEstoque.setQuantidadeAtual( barbeiro.getQuantidadeAtual() );
        dTOEstoque.setQuantidadeMinima( barbeiro.getQuantidadeMinima() );

        return dTOEstoque;
    }
}
