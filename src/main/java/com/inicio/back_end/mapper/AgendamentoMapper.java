package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOAgendamento;
import com.inicio.back_end.model.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AgendamentoMapper {

    @Mapping(target = "servico.id", source = "serviceId")
    @Mapping(target = "barbeiro.id", source = "barbeiroId")
    Agendamento toEntity(DTOAgendamento dto);

    @Mapping(target = "serviceId", source = "servico.id")
    @Mapping(target = "barbeiroId", source = "barbeiro.id")
    DTOAgendamento toDto(Agendamento entity);

}