package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOEstoque;
import com.inicio.back_end.model.Estoque;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstoqueMapper {

    Estoque toEntity(DTOEstoque dto);

    DTOEstoque toDto(Estoque barbeiro);
}
