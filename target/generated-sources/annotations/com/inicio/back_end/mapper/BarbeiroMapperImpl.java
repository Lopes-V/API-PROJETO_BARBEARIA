package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T09:59:03-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class BarbeiroMapperImpl implements BarbeiroMapper {

    @Override
    public Barbeiro toEntity(DTOBarbeiro dto) {
        if ( dto == null ) {
            return null;
        }

        Barbeiro.BarbeiroBuilder barbeiro = Barbeiro.builder();

        barbeiro.id( dto.getId_barbeiro() );
        barbeiro.ativo( dto.isAtivo() );
        barbeiro.comissao( dto.getComissao() );
        barbeiro.especialidade( dto.getEspecialidade() );
        barbeiro.nome( dto.getNome() );

        return barbeiro.build();
    }

    @Override
    public DTOBarbeiro toDto(Barbeiro barbeiro) {
        if ( barbeiro == null ) {
            return null;
        }

        DTOBarbeiro dTOBarbeiro = new DTOBarbeiro();

        dTOBarbeiro.setId_barbeiro( barbeiro.getId() );
        dTOBarbeiro.setAtivo( barbeiro.isAtivo() );
        dTOBarbeiro.setComissao( barbeiro.getComissao() );
        dTOBarbeiro.setEspecialidade( barbeiro.getEspecialidade() );
        dTOBarbeiro.setNome( barbeiro.getNome() );

        return dTOBarbeiro;
    }
}
