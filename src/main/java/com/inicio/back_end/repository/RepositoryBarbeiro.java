package com.inicio.back_end.repository;

import com.inicio.back_end.model.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryBarbeiro extends JpaRepository<Barbeiro, Long> {
}
