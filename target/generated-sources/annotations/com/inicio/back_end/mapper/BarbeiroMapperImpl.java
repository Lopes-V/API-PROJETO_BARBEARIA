package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-22T11:09:38-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class BarbeiroMapperImpl implements BarbeiroMapper {

    @Override
    public Barbeiro toEntity(DTOBarbeiro dto) {
        if ( dto == null ) {
            return null;
        }

        Barbeiro.BarbeiroBuilder barbeiro = Barbeiro.builder();

        barbeiro.nome( dto.getNome() );
        barbeiro.especialidade( dto.getEspecialidade() );
        barbeiro.comissao( dto.getComissao() );
        barbeiro.ativo( dto.isAtivo() );

        return barbeiro.build();
    }

    @Override
    public DTOBarbeiro toDto(Barbeiro barbeiro) {
        if ( barbeiro == null ) {
            return null;
        }

        DTOBarbeiro dTOBarbeiro = new DTOBarbeiro();

        dTOBarbeiro.setNome( barbeiro.getNome() );
        dTOBarbeiro.setEspecialidade( barbeiro.getEspecialidade() );
        dTOBarbeiro.setComissao( barbeiro.getComissao() );
        dTOBarbeiro.setAtivo( barbeiro.isAtivo() );

        return dTOBarbeiro;
    }
}
