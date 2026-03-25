package com.inicio.back_end.repository;

import com.inicio.back_end.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryFinanceiro extends JpaRepository<Financeiro, Long> {
}
