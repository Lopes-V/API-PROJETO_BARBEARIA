package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.mapper.BarbeiroMapper;
import com.inicio.back_end.model.Barbeiro;
import com.inicio.back_end.repository.RepositoryBarbeiro;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Primary
public class ServiceBarbeiro {

    private final RepositoryBarbeiro rb;


    private final BarbeiroMapper mM;

    public ServiceBarbeiro(RepositoryBarbeiro rb, BarbeiroMapper mM) {
        this.rb = rb;
        this.mM = mM;
    }

    @Transactional(readOnly = true)
    public List<DTOBarbeiro> get() throws RuntimeException {
        return rb.findAll().stream()
                .map(mM::toDto).toList();
    }

    @Transactional(readOnly = true)
    public DTOBarbeiro getById(Long id) throws RuntimeException {
        return rb.findById(id).map(mM::toDto).orElseThrow();
    }

    @Transactional()
    public void cadastrar(DTOBarbeiro db) throws RuntimeException {
        rb.save(mM.toEntity(db));
    }

    @Transactional()
    public void editar(DTOBarbeiro db, Long id) throws RuntimeException {
        Barbeiro barbeiroAtualizado = mM.toEntity(db);
        barbeiroAtualizado.setId(id);
        rb.save(barbeiroAtualizado);
    }

    @Transactional()
    public void deletar(Long id) throws RuntimeException {
        rb.deleteById(id);
    }
}
