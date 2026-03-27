package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOAgendamento;
import com.inicio.back_end.model.Agendamento;
import com.inicio.back_end.model.Servico;
import com.inicio.back_end.model.enums.StatusAgendamento;
import com.inicio.back_end.repository.RepositoryAgendamento;
import com.inicio.back_end.repository.RepositoryServico;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Mapper(componentModel = "spring")
public class ServiceAgendamento {

    private final RepositoryAgendamento ra;

    private final RepositoryServico rs;

    private final ModelMapper mM;

    public ServiceAgendamento(RepositoryAgendamento ra, RepositoryServico rs, ModelMapper mM) {
        this.ra = ra;
        this.rs = rs;
        this.mM = mM;
    }

    @Transactional(readOnly = true)
    public List<DTOAgendamento> get() throws RuntimeException {
        return ra.findAll().stream().map(agendamento ->
                mM.map(agendamento, DTOAgendamento.class)
        ).toList();
    }

    @Transactional
    public void criar(DTOAgendamento dtoAgendamento) throws RuntimeException {
        Servico s = rs.findById(dtoAgendamento.getServiceId()).orElseThrow();
        ra.save(mM.map(s, Agendamento.class));
    }

    @Transactional(readOnly = true)
    public List<DTOAgendamento> getByBarbeiro(Long id) throws RuntimeException {
        return ra.findAgendamentoByBarbeiroId(id).stream().map(agendamento ->
                mM.map(agendamento, DTOAgendamento.class)).toList();

    }

    @Transactional
    public void delete(Long id) {
        ra.deleteById(id);
    }

    @Transactional
    public void changeStatus(Long id, String status) {
        Agendamento a = ra.findById(id).orElseThrow();
        a.setStatusAgendamento(StatusAgendamento.valueOf(status));
        ra.save(a);
    }
}