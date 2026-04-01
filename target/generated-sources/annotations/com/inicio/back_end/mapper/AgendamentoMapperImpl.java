package com.inicio.back_end.mapper;

import com.inicio.back_end.dto.DTOAgendamento;
import com.inicio.back_end.model.Agendamento;
import com.inicio.back_end.model.Barbeiro;
import com.inicio.back_end.model.Servico;
import com.inicio.back_end.model.enums.StatusAgendamento;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T09:56:26-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class AgendamentoMapperImpl implements AgendamentoMapper {

    @Override
    public Agendamento toEntity(DTOAgendamento dto) {
        if ( dto == null ) {
            return null;
        }

        Agendamento.AgendamentoBuilder agendamento = Agendamento.builder();

        agendamento.servico( dTOAgendamentoToServico( dto ) );
        agendamento.barbeiro( dTOAgendamentoToBarbeiro( dto ) );
        agendamento.nomeCliente( dto.nomeCliente() );
        agendamento.dataInicio( dto.dataInicio() );
        agendamento.dataFim( dto.dataFim() );
        agendamento.statusAgendamento( dto.statusAgendamento() );

        return agendamento.build();
    }

    @Override
    public DTOAgendamento toDto(Agendamento entity) {
        if ( entity == null ) {
            return null;
        }

        Long serviceId = null;
        Long barbeiroId = null;
        String nomeCliente = null;
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;
        StatusAgendamento statusAgendamento = null;

        serviceId = entityServicoId( entity );
        barbeiroId = entityBarbeiroId( entity );
        nomeCliente = entity.getNomeCliente();
        dataInicio = entity.getDataInicio();
        dataFim = entity.getDataFim();
        statusAgendamento = entity.getStatusAgendamento();

        DTOAgendamento dTOAgendamento = new DTOAgendamento( nomeCliente, barbeiroId, serviceId, dataInicio, dataFim, statusAgendamento );

        return dTOAgendamento;
    }

    protected Servico dTOAgendamentoToServico(DTOAgendamento dTOAgendamento) {
        if ( dTOAgendamento == null ) {
            return null;
        }

        Servico.ServicoBuilder servico = Servico.builder();

        if ( dTOAgendamento.serviceId() != null ) {
            servico.id( dTOAgendamento.serviceId() );
        }

        return servico.build();
    }

    protected Barbeiro dTOAgendamentoToBarbeiro(DTOAgendamento dTOAgendamento) {
        if ( dTOAgendamento == null ) {
            return null;
        }

        Barbeiro.BarbeiroBuilder barbeiro = Barbeiro.builder();

        barbeiro.id( dTOAgendamento.barbeiroId() );

        return barbeiro.build();
    }

    private Long entityServicoId(Agendamento agendamento) {
        Servico servico = agendamento.getServico();
        if ( servico == null ) {
            return null;
        }
        return servico.getId();
    }

    private Long entityBarbeiroId(Agendamento agendamento) {
        Barbeiro barbeiro = agendamento.getBarbeiro();
        if ( barbeiro == null ) {
            return null;
        }
        return barbeiro.getId();
    }
}
