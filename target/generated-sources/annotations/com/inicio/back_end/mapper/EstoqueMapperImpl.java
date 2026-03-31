package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOEstoque;
import com.inicio.back_end.model.Estoque;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T09:32:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class EstoqueMapperImpl implements EstoqueMapper {

    @Override
    public Estoque toEntity(DTOEstoque dto) {
        if ( dto == null ) {
            return null;
        }

        Estoque.EstoqueBuilder estoque = Estoque.builder();

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

        dTOEstoque.setNomeItem( barbeiro.getNomeItem() );
        dTOEstoque.setQuantidadeAtual( barbeiro.getQuantidadeAtual() );
        dTOEstoque.setQuantidadeMinima( barbeiro.getQuantidadeMinima() );

        return dTOEstoque;
    }
}
