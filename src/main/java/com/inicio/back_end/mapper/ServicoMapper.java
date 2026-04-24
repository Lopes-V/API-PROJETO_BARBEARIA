package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.model.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    @Mapping(source = "id_servico", target = "id")
    Servico toEntity(DTOServico dto);

    @Mapping(source = "id", target = "id_servico")
    DTOServico toDto(Servico entity);
}
