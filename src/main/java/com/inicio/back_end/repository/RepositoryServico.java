package com.inicio.back_end.repository;

import com.inicio.back_end.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryServico extends JpaRepository<Servico, Long> {
}
