package com.inicio.back_end.repository;

import com.inicio.back_end.model.Financeiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RepositoryFinanceiro extends JpaRepository<Financeiro, Long> {
    List<Financeiro> findByDataLancamentoBetween(LocalDate start, LocalDate end);
}
