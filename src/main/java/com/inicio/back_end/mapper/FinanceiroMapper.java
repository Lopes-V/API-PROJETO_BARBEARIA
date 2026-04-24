package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.model.Financeiro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinanceiroMapper {

    Financeiro toEntity(DTOFinanceiro dto);

    DTOFinanceiro toDto(Financeiro entity);
}
