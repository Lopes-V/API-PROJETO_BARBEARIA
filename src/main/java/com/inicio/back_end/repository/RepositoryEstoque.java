package com.inicio.back_end.repository;

import com.inicio.back_end.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryEstoque extends JpaRepository<Estoque, Long> {

    @Query("SELECT e FROM Estoque e WHERE e.quantidadeAtual < e.quantidadeMinima")
    List<Estoque> buscarProdutosAbaixoDoMinimo();
}
