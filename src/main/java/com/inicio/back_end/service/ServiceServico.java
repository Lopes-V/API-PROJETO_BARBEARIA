package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOServico;
import com.inicio.back_end.mapper.ServicoMapper;
import com.inicio.back_end.repository.RepositoryServico;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ServiceServico {

    private final ServicoMapper mM;
    private final RepositoryServico rs;

    public ServiceServico(ServicoMapper mM, RepositoryServico rs) {
        this.mM = mM;
        this.rs = rs;
    }

    @Transactional(readOnly = true)
    public List<DTOServico> get() throws RuntimeException{
        return rs.findAll().stream().map(servico ->
                mM.toDto(servico)).toList();
    }

    @Transactional(readOnly = true)
    public DTOServico getById(Long id) throws RuntimeException{
        return rs.findById(id).map(s -> mM.toDto(s)).orElseThrow();
    }

    @Transactional
    public void criar(DTOServico dS) throws RuntimeException{
        rs.save(mM.toEntity(dS));
    }

    @Transactional
    public void deletar(Long id) throws RuntimeException{
        try{
            if(!rs.existsById(id)){
                throw new RuntimeException("Servico não encontrado");
            }else{
                deletar(id);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
