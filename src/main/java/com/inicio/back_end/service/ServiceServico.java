package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.model.Servico;
import com.inicio.back_end.repository.RepositoryServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServico {

    @Autowired
    private  ModelMapper mM;
    private final RepositoryServico rs;

    public ServiceServico(RepositoryServico rs) {
        this.rs = rs;
    }

    @Transactional(readOnly = true)
    public List<DTOServico> get() throws RuntimeException{
        return rs.findAll().stream().map(servico ->
                mM.map(servico, DTOServico.class)).toList();
    }

    @Transactional(readOnly = true)
    public DTOServico getById(Long id) throws RuntimeException{
        return rs.findById(id).map(s -> mM.map(s, DTOServico.class)).orElseThrow();
    }

    @Transactional
    public void criar(DTOServico dS) throws RuntimeException{
        Servico s = mM.map(dS, Servico.class);
        rs.save(s);
    }

    @Transactional
    public void deletar(Long id) throws RuntimeException{
        deletar(id);
    }
}
