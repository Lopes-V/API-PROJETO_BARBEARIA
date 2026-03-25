package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOBarbeiro;
import com.inicio.back_end.model.Barbeiro;
import com.inicio.back_end.repository.RepositoryAgendamento;
import com.inicio.back_end.repository.RepositoryBarbeiro;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServiceBarbeiro {

    private final RepositoryBarbeiro rb;

    private final RepositoryAgendamento ra;

    public ServiceBarbeiro(RepositoryBarbeiro rb, RepositoryAgendamento ra) {
        this.rb = rb;
        this.ra = ra;
    }

    public List<DTOBarbeiro> get() throws RuntimeException {
        return rb.findAll().stream()
                .map(b -> new DTOBarbeiro(
                        b.getNome() != null ? b.getNome() : "Sem Nome",
                        b.getEspecialidade(),
                        b.getComissao(),
                        b.isAtivo()
                ))
                .toList();
    }

    public DTOBarbeiro getById(Long id) throws RuntimeException {
        Barbeiro b = rb.findById(id).orElseThrow(RuntimeException::new);
        return new DTOBarbeiro(b.getNome(), b.getEspecialidade(), b.getComissao(), b.isAtivo());
    }

    public void cadastrar(DTOBarbeiro db) throws RuntimeException {
        rb.save(Barbeiro.builder().nome(db.nome()).ativo(db.ativo()).especialidade(db.especialidade()).comissao(db.comissao()).build());
    }

    public void editar(DTOBarbeiro db, Long id) throws RuntimeException {
        Barbeiro b = rb.findById(id).orElseThrow(RuntimeException::new);
        b.setAtivo(db.ativo());
        b.setNome(db.nome());
        b.setComissao(db.comissao());
        b.setEspecialidade(db.especialidade());
        rb.save(b);
    }

    public void deletar(Long id) throws RuntimeException {
        if (rb.findById(id).isPresent()) {
            if (!ra.existsByBarbeiroId(id)) {
                rb.deleteById(id);
            } else {
                Barbeiro b = rb.findById(id).orElseThrow();
                b.setAtivo(false);
                rb.save(b);
                throw new RuntimeException();
            }
        }else{
            throw new RuntimeException("barbeiro não encontrado");
        }
    }
}
