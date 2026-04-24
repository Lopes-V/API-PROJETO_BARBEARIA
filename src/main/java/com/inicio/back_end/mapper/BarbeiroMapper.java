package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarbeiroMapper {

    @Mapping(source = "id_barbeiro", target = "id")
    Barbeiro toEntity(DTOBarbeiro dto);

    @Mapping(source = "id", target = "id_barbeiro")
    DTOBarbeiro toDto(Barbeiro barbeiro);
}
