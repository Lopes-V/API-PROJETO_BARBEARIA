package com.inicio.back_end.mapper;


import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.model.Financeiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinanceiroMapper {

    Financeiro toEntity(DTOFinanceiro dto);

    DTOFinanceiro toDto(Financeiro entity);
}
