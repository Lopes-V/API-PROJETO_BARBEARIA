package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOAgendamento;
import com.inicio.back_end.dto.especifico.DTOPatchAgendamento;
import com.inicio.back_end.mapper.AgendamentoMapper;
import com.inicio.back_end.model.Agendamento;
import com.inicio.back_end.model.enums.StatusAgendamento;
import com.inicio.back_end.repository.RepositoryAgendamento;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class ServiceAgendamento {

    private final RepositoryAgendamento ra;
    private final AgendamentoMapper mM;

    public ServiceAgendamento(RepositoryAgendamento ra, AgendamentoMapper mM) {
        this.ra = ra;
        this.mM = mM;
    }


    @Transactional(readOnly = true)
    public List<DTOAgendamento> get() throws RuntimeException {
        return ra.findAll().stream().map(mM::toDto
        ).toList();
    }

    @Transactional
    public Agendamento criar(DTOAgendamento dtoAgendamento) throws RuntimeException {
        return ra.save(mM.toEntity(dtoAgendamento));
    }

    @Transactional(readOnly = true)
    public List<DTOAgendamento> getByBarbeiro(Long id) throws RuntimeException {
        return ra.findAgendamentoByBarbeiroId(id).stream().map(mM::toDto).toList();

    }

    @Transactional
    public void delete(Long id) throws RuntimeException{
        ra.deleteById(id);
    }

    @Transactional
    public void changeStatus(Long id, DTOPatchAgendamento status) {
        Agendamento a = ra.findById(id).orElseThrow();
        a.setStatusAgendamento(StatusAgendamento.valueOf(status.statusAgendamento()));
        ra.save(a);
    }
}