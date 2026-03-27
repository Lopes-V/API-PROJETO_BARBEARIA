package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BarbeiroMapper {

    Barbeiro toEntity(DTOBarbeiro dto);

    DTOBarbeiro toDto(Barbeiro barbeiro);
}
