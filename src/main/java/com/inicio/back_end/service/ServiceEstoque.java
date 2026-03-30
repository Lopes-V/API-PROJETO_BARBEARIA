package com.inicio.back_end.service;


import com.inicio.back_end.dto.DTOEstoque;
import com.inicio.back_end.dto.especifico.DTOPatchEstoque;
import com.inicio.back_end.mapper.EstoqueMapper;
import com.inicio.back_end.model.Estoque;
import com.inicio.back_end.repository.RepositoryEstoque;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceEstoque {

    private final RepositoryEstoque repositoryEstoque;
    private final EstoqueMapper estoqueMapper;

    public ServiceEstoque(RepositoryEstoque repositoryEstoque, EstoqueMapper estoqueMapper) throws RuntimeException{
        this.repositoryEstoque = repositoryEstoque;
        this.estoqueMapper = estoqueMapper;
    }

    @Transactional(readOnly = true)
    public List<DTOEstoque> get() throws RuntimeException{
        return repositoryEstoque.findAll().stream().map(estoqueMapper::toDto).toList();
    }
    @Transactional(readOnly = true)
    public List<DTOEstoque> getAbaixo(){
        return repositoryEstoque.buscarProdutosAbaixoDoMinimo().stream().map(estoqueMapper::toDto).toList();
    }

    @Transactional
    public Estoque cadastrar(DTOEstoque dtoEstoque) throws RuntimeException{
        return repositoryEstoque.save(estoqueMapper.toEntity(dtoEstoque));
    }

    @Transactional
    public void movimentacao(DTOPatchEstoque dtoPatchEstoque) throws RuntimeException{
        Estoque estoque = repositoryEstoque.findById(dtoPatchEstoque.id()).orElseThrow();
        if ((dtoPatchEstoque.quantidade() < 0)) {
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() - dtoPatchEstoque.quantidade());
        }else {
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + dtoPatchEstoque.quantidade());
        }
        repositoryEstoque.save(estoque);
    }
}
