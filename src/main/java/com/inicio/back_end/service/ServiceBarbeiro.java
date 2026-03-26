package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import com.inicio.back_end.repository.RepositoryBarbeiro;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ServiceBarbeiro {

    private final RepositoryBarbeiro rb;


    private final ModelMapper mM;

    public ServiceBarbeiro(RepositoryBarbeiro rb, ModelMapper mM) {
        this.rb = rb;
        this.mM = mM;
    }

    @Transactional(readOnly = true)
    public List<DTOBarbeiro> get() throws RuntimeException {
        return rb.findAll().stream()
                .map(b ->
                        mM.map(b, DTOBarbeiro.class)).toList();
    }

    @Transactional(readOnly = true)
    public DTOBarbeiro getById(Long id) throws RuntimeException {
        return rb.findById(id).map(b -> mM.map(b, DTOBarbeiro.class)).orElseThrow();
    }

    @Transactional()
    public void cadastrar(DTOBarbeiro db) throws RuntimeException {
        rb.save(mM.map(db, Barbeiro.class));
    }

    @Transactional()
    public void editar(DTOBarbeiro db, Long id) throws RuntimeException {
        Barbeiro b = rb.findById(id).orElseThrow(RuntimeException::new);
        mM.map(db, b);
        rb.save(b);
    }

    @Transactional()
    public void deletar(Long id) throws RuntimeException {
        rb.deleteById(id);
    }
}
