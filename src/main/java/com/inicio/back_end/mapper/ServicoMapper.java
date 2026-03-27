package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.model.Servico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    Servico toEntity(DTOServico dto);

    DTOServico toDto(Servico entity);
}
