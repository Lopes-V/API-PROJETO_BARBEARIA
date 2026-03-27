package com.inicio.back_end.mapper;


import com.inicio.back_end.dto.DTOFinanceiro;
import com.inicio.back_end.model.Financeiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinanceiroMapper {

    @Mapping(target = "agendamento.id", source = "agendamentoId")
    Financeiro toEntity(DTOFinanceiro dto);

    @Mapping(target = "agendamentoId", source = "agendamento.id")
    DTOFinanceiro toDto(Financeiro entity);
}
