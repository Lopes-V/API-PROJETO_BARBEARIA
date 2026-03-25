package com.inicio.back_end.repository;

import com.inicio.back_end.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEstoque extends JpaRepository<Estoque, Long> {
}
