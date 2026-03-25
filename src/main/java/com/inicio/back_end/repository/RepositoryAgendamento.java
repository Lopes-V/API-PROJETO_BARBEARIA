package com.inicio.back_end.repository;

import com.inicio.back_end.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryAgendamento extends JpaRepository<Agendamento, Long> {
    boolean existsByBarbeiroId(Long barbeiroId);
}
